package com.example.genericagent.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class AgentService {

    private static final int MAX_ITERATIONS = 5;

    private final BedrockAiExtractor bedrockAiExtractor;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;
    private final String promptTemplate;

    public AgentService(BedrockAiExtractor bedrockAiExtractor,
                        ObjectMapper objectMapper,
                        @Value("${baseUrl}") String baseUrl) throws IOException {
        this.bedrockAiExtractor = bedrockAiExtractor;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
        this.restTemplate = new RestTemplate();
        this.promptTemplate = loadPromptTemplate();
    }


    public Map<String, Object> handle(String userPrompt) throws Exception {

        List<Map<String, Object>> workflow = new ArrayList<>();
        StringBuilder conversationHistory = new StringBuilder();

        conversationHistory.append(buildSystemPrompt());
        conversationHistory.append("\nUser: ").append(userPrompt).append("\n");

        int iteration = 0;

        while (iteration < MAX_ITERATIONS) {
            iteration++;

            // 1. Call AI
            String aiRaw = bedrockAiExtractor.invoke(conversationHistory.toString());

            // 2. Extract text from Bedrock response
            Map<String, Object> responseMap = objectMapper.readValue(aiRaw, Map.class);
            List<Map<String, Object>> content = (List<Map<String, Object>>) responseMap.get("content");
            String responseText = (String) content.get(0).get("text");

            // 3. Check if AI is done
            if (isDone(responseText)) {
                return buildResponse(userPrompt, workflow, extractFinalMessage(responseText));
            }

            // 4. Clean and parse JSON
            String cleanedJson = cleanJson(responseText);
            if (cleanedJson.equals("{}")) {
                return buildResponse(userPrompt, workflow, responseText);
            }

            Map<String, Object> decision = objectMapper.readValue(cleanedJson, Map.class);
            String toolName = (String) decision.get("tool");
            Map<String, Object> args = (Map<String, Object>) decision.get("arguments");
            if (args == null) args = Map.of();

            // 5. Handle chat tool
            if ("chat".equals(toolName)) {
                String message = args.containsKey("message")
                        ? (String) args.get("message")
                        : responseText;
                return buildResponse(userPrompt, workflow, message);
            }

            // 6. Execute tool by calling MCP server directly via baseUrl
            Object result = callMcpTool(toolName, args);

            // 7. Record step
            Map<String, Object> step = new LinkedHashMap<>();
            step.put("action", toolName);
            step.put("parameters", args);
            step.put("result", result);
            workflow.add(step);

            // 8. Update conversation history with result
            conversationHistory.append("Assistant used tool: ").append(toolName).append("\n");
            conversationHistory.append("Tool result: ")
                    .append(objectMapper.writeValueAsString(result)).append("\n");
            conversationHistory.append("What should I do next? If all tasks are done, reply with DONE: <your summary>\n");
        }

        // Max iterations reached
        return buildResponse(userPrompt, workflow, "Reached maximum steps. Here is what was completed so far.");
    }

    /**
     * Calls POST baseUrl (http://localhost:8081/mcp/call) with {"tool": toolName, "args": args}
     */
    private Object callMcpTool(String toolName, Map<String, Object> args) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("tool", toolName);
        body.put("args", args);

        return restTemplate.postForObject(baseUrl, body, Object.class);
    }

    /**
     * Calls GET http://localhost:8081/mcp/tools to discover available tools
     * and builds a text section for the system prompt.
     */
  
    private String fetchToolsDescription() {
        try {
            // Derive tools URL from baseUrl: replace /mcp/call with /mcp/tools
            String toolsUrl = baseUrl.replace("/mcp/call", "/mcp/tools");
            Object response = restTemplate.getForObject(toolsUrl, Object.class);
            String json = objectMapper.writeValueAsString(response);
            List<Map<String, Object>> tools = objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, Map.class));

            StringBuilder sb = new StringBuilder();
            for (Map<String, Object> tool : tools) {
                sb.append("- ").append(tool.get("name"))
                        .append(": ").append(tool.get("description")).append("\n");

                // Parse parameters from inputSchema (MCP standard)
                Map<String, Object> inputSchema = (Map<String, Object>) tool.get("inputSchema");
                if (inputSchema != null) {
                    Map<String, Object> properties = (Map<String, Object>) inputSchema.get("properties");
                    List<String> required = (List<String>) inputSchema.getOrDefault("required", List.of());
                    if (properties != null) {
                        sb.append("  Parameters:\n");
                        for (Map.Entry<String, Object> entry : properties.entrySet()) {
                            Map<String, Object> propDef = (Map<String, Object>) entry.getValue();
                            sb.append("    - ").append(entry.getKey())
                                    .append(" (").append(propDef.getOrDefault("type", "string")).append(")")
                                    .append(required.contains(entry.getKey()) ? " [required]" : " [optional]")
                                    .append("\n");
                        }
                    }
                }
            }
            return sb.toString();

        } catch (Exception e) {
            System.err.println(" Failed to fetch tools from MCP server: " + e.getMessage());
            return "- No tools available (MCP server unreachable)\n";
        }
    }

    private Map<String, Object> buildResponse(String prompt, List<Map<String, Object>> workflow, String aiResponse) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("prompt", prompt);
        response.put("workflow", workflow);
        response.put("ai_response", aiResponse);
        return response;
    }

    private String loadPromptTemplate() throws IOException {
        ClassPathResource resource = new ClassPathResource("prompts/system-prompt.txt");
        return new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
    }

    private String buildSystemPrompt() {
        String toolsSection = fetchToolsDescription();
        return promptTemplate.replace("{{tools}}", toolsSection);
    }

    private boolean isDone(String text) {
        return text != null && text.trim().toUpperCase().startsWith("DONE:");
    }

    private String extractFinalMessage(String text) {
        if (text != null && text.length() > 5) {
            return text.substring(5).trim();
        }
        return "All tasks completed!";
    }

    private String cleanJson(String text) {
        if (text == null) return "{}";
        text = text.replaceAll("```json", "").replaceAll("```", "").trim();
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start >= 0 && end > start) {
            return text.substring(start, end + 1);
        }
        return "{}";
    }
}

package com.example.mcp.tool;

import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.example.mcp.tool.model.ToolDefinition;
import com.example.mcp.tool.model.ToolParameter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ToolDispatcher {

    private final List<ToolDefinition> tools;

    public ToolDispatcher(List<ToolDefinition> tools) {
        this.tools = tools;
    }

    public Object dispatch(String toolName, Map<String, Object> args) {

        // 1. Find the tool definition by name
        ToolDefinition tool = tools.stream()
                .filter(t -> t.getName().equals(toolName))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Unknown tool: " + toolName));

        // 2. Separate path params and body params
        String resolvedUrl = tool.getUrl();
        Map<String, Object> bodyParams = new HashMap<>();

        for (ToolParameter param : tool.getParameters()) {
            Object value = args.get(param.getName());
            if (value == null) continue;

            if ("path".equals(param.getLocation())) {
                // Replace {id} with actual value in URL
                resolvedUrl = resolvedUrl.replace("{" + param.getName() + "}", value.toString());
            } else {
                // Add to body
                bodyParams.put(param.getName(), value);
            }
        }

        // 3. Build WebClient dynamically
        WebClient client = WebClient.create(tool.getBaseUrl());

        // 4. Execute based on HTTP method
        return switch (tool.getHttpMethod()) {
            case "GET" -> client.get()
                    .uri(resolvedUrl)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("API Error: " + body))
                    )
                    .bodyToMono(Object.class)
                    .block();

            case "POST" -> client.post()
                    .uri(resolvedUrl)
                    .bodyValue(bodyParams)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("API Error: " + body))
                    )
                    .bodyToMono(Object.class)
                    .block();

            case "PUT" -> client.put()
                    .uri(resolvedUrl)
                    .bodyValue(bodyParams)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, response ->
                            response.bodyToMono(String.class)
                                    .map(body -> new RuntimeException("API Error: " + body))
                    )
                    .bodyToMono(Object.class)
                    .block();

            case "DELETE" -> {
                try {
                    client.delete()
                            .uri(resolvedUrl)
                            .retrieve()
                            .onStatus(HttpStatusCode::isError, response ->
                                    response.bodyToMono(String.class)
                                            .map(body -> new RuntimeException("API Error: " + body))
                            )
                            .bodyToMono(Void.class)
                            .block();
                    yield Map.of("status", "deleted", "tool", toolName);
                } catch (Exception e) {
                    yield Map.of("status", "error", "message", e.getMessage());
                }
            }
            default -> throw new RuntimeException("Unsupported HTTP method: " + tool.getHttpMethod());
        };
    }
}

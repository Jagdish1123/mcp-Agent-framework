package com.example.mcp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.mcp.tool.ToolDispatcher;
import com.example.mcp.tool.model.ToolDefinition;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/mcp")
public class McpController {

    private final ToolDispatcher toolDispatcher;
    private final List<ToolDefinition> tools;

    public McpController(ToolDispatcher toolDispatcher, List<ToolDefinition> tools) {
        this.toolDispatcher = toolDispatcher;
        this.tools = tools;
    }

    @GetMapping
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("MCP endpoint is active at http://localhost:8081/mcp");
    }

    // Endpoint to discover available tools
    @GetMapping("/tools")
    public ResponseEntity<List<ToolDefinition>> listTools() {
        return ResponseEntity.ok(tools);
    }

    // Endpoint to execute a tool (Acting as MCP call_tool)
    @PostMapping("/call")
    public ResponseEntity<Object> callTool(@RequestBody McpRequest request) {
        try {
            Object result = toolDispatcher.dispatch(request.getTool(), request.getArgs());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DTO for MCP requests
    public static class McpRequest {
        private String tool;
        private Map<String, Object> args;

        public String getTool() { return tool; }
        public void setTool(String tool) { this.tool = tool; }
        public Map<String, Object> getArgs() { return args; }
        public void setArgs(Map<String, Object> args) { this.args = args; }
    }
}

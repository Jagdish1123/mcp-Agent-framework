package com.example.mcp.tool.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ToolDefinition {
    private String name;                    // "create_note"
    private String description;             // for AI prompt
    private ToolType type;                  // REST only for now
    private String httpMethod;              // "POST", "GET", "PUT", "DELETE"
    private String baseUrl;                 // "http://localhost:8080/api"
    private String url;                     // "/notes" or "/notes/{id}"
    private List<ToolParameter> parameters;
}

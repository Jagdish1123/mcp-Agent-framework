package com.example.mcp.tool.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ToolParameter {
    private String name;      // "title", "id"
    private String type;      // "string", "uuid"
    private boolean required;
    private String location;  // "body" or "path"
}
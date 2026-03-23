package com.example.mcp.tool.model;

import lombok.Builder;
import lombok.Data;
import java.util.Map;

@Data
@Builder
public class AgentStep {
    private String action;
    private Map<String, Object> parameters;
    private Object result;
}
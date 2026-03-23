package com.example.mcp.tool.model;


import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class AgentResponse {
    private String prompt;
    private List<AgentStep> workflow;
    private String ai_response;
}
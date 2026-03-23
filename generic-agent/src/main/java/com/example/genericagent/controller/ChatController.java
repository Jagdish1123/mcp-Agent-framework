package com.example.genericagent.controller;

import com.example.genericagent.ai.AgentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final AgentService agentService;

    public ChatController(AgentService agentService) {
        this.agentService = agentService;
    }

    @PostMapping
    public Object chat(@RequestBody Map<String, String> request) throws Exception {
        String prompt = request.get("prompt");

        if (prompt == null || prompt.isEmpty()) {
            throw new IllegalArgumentException("Prompt must not be empty");
        }

        return agentService.handle(prompt);
    }


}
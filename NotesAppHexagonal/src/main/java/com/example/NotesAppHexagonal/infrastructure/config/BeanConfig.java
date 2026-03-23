package com.example.NotesAppHexagonal.infrastructure.config;

import com.example.NotesAppHexagonal.application.ports.in.NoteServicePort;
import com.example.NotesAppHexagonal.application.ports.out.NoteRepositoryPort;
import com.example.NotesAppHexagonal.domain.service.NoteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {

    @Value("${app.gemini.api-key}")
    private String geminiApiKey;

    @Bean
    public NoteServicePort noteServicePort(NoteRepositoryPort noteRepositoryPort) {
        return new NoteService(noteRepositoryPort);
    }

    @Bean
    public WebClient geminiWebClient() {
        return WebClient.builder()
                .baseUrl("https://generativelanguage.googleapis.com")
                .defaultHeader("X-Goog-Api-Key", geminiApiKey)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
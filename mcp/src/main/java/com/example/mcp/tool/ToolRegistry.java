package com.example.mcp.tool;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.mcp.tool.model.ToolDefinition;
import com.example.mcp.tool.model.ToolParameter;
import com.example.mcp.tool.model.ToolType;

import java.util.List;

@Configuration
public class ToolRegistry {

    @Bean
    public List<ToolDefinition> tools(@org.springframework.beans.factory.annotation.Value("${notes.baseUrl}") String notesBaseUrl) {
        return List.of(

                ToolDefinition.builder()
                        .name("create_note")
                        .description("Creates a new note with a title and description")
                        .type(ToolType.REST)
                        .httpMethod("POST")
                        .baseUrl(notesBaseUrl)
                        .url("/notes")
                        .parameters(List.of(
                                ToolParameter.builder().name("title").type("string").required(true).location("body").build(),
                                ToolParameter.builder().name("description").type("string").required(false).location("body").build()
                        ))
                        .build(),

                ToolDefinition.builder()
                        .name("get_note")
                        .description("Gets a note by its ID")
                        .type(ToolType.REST)
                        .httpMethod("GET")
                        .baseUrl(notesBaseUrl)
                        .url("/notes/{id}")
                        .parameters(List.of(
                                ToolParameter.builder().name("id").type("uuid").required(true).location("path").build()
                        ))
                        .build(),

                ToolDefinition.builder()
                        .name("list_notes")
                        .description("Returns all notes")
                        .type(ToolType.REST)
                        .httpMethod("GET")
                        .baseUrl(notesBaseUrl)
                        .url("/notes")
                        .parameters(List.of())
                        .build(),

                ToolDefinition.builder()
                        .name("update_note")
                        .description("Updates an existing note by ID")
                        .type(ToolType.REST)
                        .httpMethod("PUT")
                        .baseUrl(notesBaseUrl)
                        .url("/notes/{id}")
                        .parameters(List.of(
                                ToolParameter.builder().name("id").type("uuid").required(true).location("path").build(),
                                ToolParameter.builder().name("title").type("string").required(false).location("body").build(),
                                ToolParameter.builder().name("description").type("string").required(false).location("body").build()
                        ))
                        .build(),

                ToolDefinition.builder()
                        .name("delete_note")
                        .description("Deletes a note by ID")
                        .type(ToolType.REST)
                        .httpMethod("DELETE")
                        .baseUrl(notesBaseUrl)
                        .url("/notes/{id}")
                        .parameters(List.of(
                                ToolParameter.builder().name("id").type("uuid").required(true).location("path").build()
                        ))
                        .build()
        );
    }
}
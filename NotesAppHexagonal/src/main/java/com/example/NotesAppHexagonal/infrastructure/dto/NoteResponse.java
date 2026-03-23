package com.example.NotesAppHexagonal.infrastructure.dto;

import lombok.Data;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoteResponse {
    private UUID publicId;
    private String title;
    private String description;
    private Instant dateCreated;
    private Instant dateModified;
}

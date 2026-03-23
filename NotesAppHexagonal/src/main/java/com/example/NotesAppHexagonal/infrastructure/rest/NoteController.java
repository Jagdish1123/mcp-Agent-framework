package com.example.NotesAppHexagonal.infrastructure.rest;


import com.example.NotesAppHexagonal.application.ports.in.NoteServicePort;
import com.example.NotesAppHexagonal.domain.model.Note;
import com.example.NotesAppHexagonal.infrastructure.dto.NoteRequest;
import com.example.NotesAppHexagonal.infrastructure.dto.NoteResponse;
import com.example.NotesAppHexagonal.infrastructure.mappers.NoteRestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class NoteController {

    private final NoteServicePort noteServicePort; // The Input Port
    private final NoteRestMapper noteRestMapper;   // The Infrastructure Mapper

    public NoteController(NoteServicePort noteServicePort, NoteRestMapper noteRestMapper) {
        this.noteServicePort = noteServicePort;
        this.noteRestMapper = noteRestMapper;
    }

    @PostMapping("/notes")
    public ResponseEntity<NoteResponse> createNote(@RequestBody NoteRequest noteRequest) {
        // 1. DTO -> Domain
        Note noteDomain = noteRestMapper.toDomain(noteRequest);

        // 2. Call the Hexagon (Domain Logic)
        Note savedNote = noteServicePort.createNote(noteDomain);

        // 3. Domain -> DTO
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(noteRestMapper.toResponse(savedNote));
    }

    @GetMapping("/notes")
    public ResponseEntity<List<NoteResponse>> getAllNotes() {
        List<Note> notes = noteServicePort.getAllNotes();
        return ResponseEntity.ok(noteRestMapper.toResponseList(notes));
    }

    @GetMapping("/notes/{id}")
    public ResponseEntity<NoteResponse> getNoteById(@PathVariable UUID id) {
        Note note = noteServicePort.getNoteByPublicId(id);
        return ResponseEntity.ok(noteRestMapper.toResponse(note));
    }

    @PutMapping("/notes/{id}")
    public ResponseEntity<NoteResponse> updateNote(@PathVariable UUID id, @RequestBody NoteRequest noteRequest) {
        Note noteUpdate = noteRestMapper.toDomain(noteRequest);
        Note updatedNote = noteServicePort.updateNote(id, noteUpdate);
        return ResponseEntity.ok(noteRestMapper.toResponse(updatedNote));
    }

    @DeleteMapping("/notes/{id}")
    public ResponseEntity<Void> deleteNote(@PathVariable UUID id) {
        noteServicePort.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
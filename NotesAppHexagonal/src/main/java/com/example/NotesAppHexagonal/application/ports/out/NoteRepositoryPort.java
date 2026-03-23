package com.example.NotesAppHexagonal.application.ports.out;

import com.example.NotesAppHexagonal.domain.model.Note;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteRepositoryPort {
    Note save(Note note);
    List<Note> findAll();
    Optional<Note> findByPublicId(UUID id);
    void delete(Note note);

    List<Note> searchByKeyword(String keyword);
}
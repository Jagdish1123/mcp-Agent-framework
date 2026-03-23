package com.example.NotesAppHexagonal.application.ports.in;

import com.example.NotesAppHexagonal.domain.model.Note;

import java.util.List;
import java.util.UUID;

public interface NoteServicePort {

    Note createNote(Note note);

    List<Note> getAllNotes();

    Note getNoteByPublicId(UUID id);

    Note updateNote(UUID id, Note note);

    void deleteNote(UUID id);

    List<Note> searchNotes(String keyword);
}
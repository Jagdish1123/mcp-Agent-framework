package com.example.NotesAppHexagonal.domain.service;

import com.example.NotesAppHexagonal.application.ports.in.NoteServicePort;
import com.example.NotesAppHexagonal.application.ports.out.NoteRepositoryPort;
import com.example.NotesAppHexagonal.domain.model.Note;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NoteService implements NoteServicePort {

    private final NoteRepositoryPort noteRepositoryPort;

    @Override
    public Note createNote(Note note) {
        if (note.getTitle() == null || note.getTitle().isBlank()) {
            System.out.println("Title is null or empty");
        }
        
        note.setPublicId(UUID.randomUUID());
        return noteRepositoryPort.save(note);
    }

    @Override
    public List<Note> getAllNotes() {
        return noteRepositoryPort.findAll();
    }

    @Override
    public Note getNoteByPublicId(UUID id) {
        return noteRepositoryPort.findByPublicId(id)
                .orElseThrow(() -> new RuntimeException("Note Not Found"));
    }

    @Override
    public Note updateNote(UUID id, Note noteUpdate) {
        Note existingNote = getNoteByPublicId(id);
        existingNote.setTitle(noteUpdate.getTitle());
        existingNote.setDescription(noteUpdate.getDescription());
        return noteRepositoryPort.save(existingNote);
    }

    @Override
    public void deleteNote(UUID id) {
        Note note = getNoteByPublicId(id);
        noteRepositoryPort.delete(note);
    }

    @Override
    public List<Note> searchNotes(String keyword) {

        if (keyword == null || keyword.isBlank()) {
            throw new RuntimeException("Keyword cannot be empty");
        }

        return noteRepositoryPort.searchByKeyword(keyword);
    }
}
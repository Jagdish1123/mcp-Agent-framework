package com.example.NotesAppHexagonal.infrastructure.mappers;

import com.example.NotesAppHexagonal.domain.model.Note;
import com.example.NotesAppHexagonal.infrastructure.dto.NoteRequest;
import com.example.NotesAppHexagonal.infrastructure.dto.NoteResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NoteRestMapper {

    public Note toDomain(NoteRequest request) {
        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setDescription(request.getDescription());
        return note;
    }

    public NoteResponse toResponse(Note domain) {
        NoteResponse response = new NoteResponse();
        response.setPublicId(domain.getPublicId());
        response.setTitle(domain.getTitle());
        response.setDescription(domain.getDescription());
        response.setDateCreated(domain.getDateCreated());
        response.setDateModified(domain.getDateModified());
        return response;
    }

    public List<NoteResponse> toResponseList(List<Note> notes) {
        return notes.stream().map(this::toResponse).toList();
    }
}

package com.example.NotesAppHexagonal.infrastructure.mappers;

import com.example.NotesAppHexagonal.domain.model.Note;
import com.example.NotesAppHexagonal.infrastructure.persistence.entity.NoteEntity;
import org.springframework.stereotype.Component;

@Component
public class NotePersistenceMapper {

    public Note toDomain(NoteEntity entity) {
        if (entity == null) return null;

        return new Note(
                entity.getId(),
                entity.getPublicId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDateCreated(),
                entity.getDateModified()
        );
    }

    public NoteEntity toEntity(Note domain) {
        if (domain == null) return null;

        NoteEntity entity = new NoteEntity();
        entity.setId(domain.getId());
        entity.setPublicId(domain.getPublicId());
        entity.setTitle(domain.getTitle());
        entity.setDescription(domain.getDescription());
        // dateCreated and dateModified are handled by @CreatedDate/@LaModifiedDate in NoteEntity
        return entity;
    }
}
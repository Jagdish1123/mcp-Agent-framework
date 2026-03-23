package com.example.NotesAppHexagonal.infrastructure.adaptors;

import com.example.NotesAppHexagonal.application.ports.out.NoteRepositoryPort;
import com.example.NotesAppHexagonal.domain.model.Note;
import com.example.NotesAppHexagonal.infrastructure.persistence.entity.NoteEntity;
import com.example.NotesAppHexagonal.infrastructure.mappers.NotePersistenceMapper;
import com.example.NotesAppHexagonal.infrastructure.persistence.repository.NoteJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class NotePersistenceAdapter implements NoteRepositoryPort {

    private final NoteJpaRepository jpaRepository;
    private final NotePersistenceMapper mapper;

    @Override
    public Note save(Note note) {
        // 1. Map Domain -> Entity
        NoteEntity entity = mapper.toEntity(note);

        // 2. Perform DB operation
        NoteEntity savedEntity = jpaRepository.save(entity);

        // 3. Map Entity -> Domain
        return mapper.toDomain(savedEntity);
    }

    @Override
    public List<Note> findAll() {
        return jpaRepository.findAll()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<Note> findByPublicId(UUID id) {
        return jpaRepository.findByPublicId(id)
                .map(mapper::toDomain);
    }

    @Override
    public void delete(Note note) {
        // We map the domain object to an entity so JPA knows what to delete
        NoteEntity entity = mapper.toEntity(note);
        jpaRepository.delete(entity);
    }

    @Override
    public List<Note> searchByKeyword(String keyword) {
        return jpaRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
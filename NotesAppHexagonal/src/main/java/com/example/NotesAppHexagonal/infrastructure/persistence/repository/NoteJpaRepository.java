package com.example.NotesAppHexagonal.infrastructure.persistence.repository;

import com.example.NotesAppHexagonal.infrastructure.persistence.entity.NoteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NoteJpaRepository extends JpaRepository<NoteEntity, Long> {

    Optional<NoteEntity> findByPublicId(UUID publicId);

    List<NoteEntity> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String description
    );
}
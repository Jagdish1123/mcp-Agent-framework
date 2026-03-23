package com.example.NotesAppHexagonal.domain.model;

import java.time.Instant;
import java.util.UUID;

public class Note {
    private Long id;
    private UUID publicId;
    private String title;
    private String description;
    private Instant dateCreated;
    private Instant dateModified;

    public Note(Long id, UUID publicId, String title, String description, Instant dateCreated, Instant dateModified) {
        this.id = id;
        this.publicId = publicId;
        this.title = title;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateModified = dateModified;
    }

    public Note() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getPublicId() {
        return publicId;
    }

    public void setPublicId(UUID publicId) {
        this.publicId = publicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Instant getDateModified() {
        return dateModified;
    }

    public void setDateModified(Instant dateModified) {
        this.dateModified = dateModified;
    }
}

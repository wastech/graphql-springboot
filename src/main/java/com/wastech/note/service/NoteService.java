package com.wastech.note.service;

import com.wastech.note.model.Category;
import com.wastech.note.model.Note;
import com.wastech.note.repository.NoteRepository;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    public Note findById(Long id) {
        return noteRepository.findById(id).orElse(null);
    }

    public List<Note> findByCategory(Category category) {
        return noteRepository.findByCategory(category);
    }

    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }

    @Transactional
    public Note createNote(
        @Argument String imageUrl,
        @Argument String title,
        @Argument String description,
        @Argument Category category) {

        Note note = Note.builder()
            .imageUrl(imageUrl)
            .title(title)
            .description(description)
            .category(category)
            .createdAt(OffsetDateTime.now())
            .updatedAt(OffsetDateTime.now())
            .build();

        return noteRepository.save(note);
    }

    public Note updateNote(String id, String imageUrl, String title, String description, Category category) {
        if (id == null) {
            throw new NoResultException("Note ID cannot be null");
        }

        Long noteId;
        try {
            noteId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NoResultException("Invalid note ID format: " + id);
        }

        Optional<Note> existingNote = noteRepository.findById(noteId);

        if (existingNote.isEmpty()) {
            throw new NoResultException("Note not found with ID: " + id);
        }

        Note note = existingNote.get();

        // Only update fields that are not null
        if (imageUrl != null) {
            note.setImageUrl(imageUrl);
        }
        if (title != null) {
            note.setTitle(title);
        }
        if (description != null) {
            note.setDescription(description);
        }
        if (category != null) {
            note.setCategory(category);
        }

        return noteRepository.save(note);
    }

    public Boolean deleteNoteById(String id) {
        if (id == null) {
            throw new NoResultException("Note ID cannot be null");
        }

        Long noteId;
        try {
            noteId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            throw new NoResultException("Invalid note ID format: " + id);
        }

        if (!noteRepository.existsById(noteId)) {
            throw new NoResultException("Cannot delete - Note not found with ID: " + id);
        }

        try {
            noteRepository.deleteById(noteId);
            return true;
        } catch (Exception e) {
            throw new NoResultException("Failed to delete note with ID: " +  e);
        }
    }
}

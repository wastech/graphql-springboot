package com.wastech.note.service;

import com.wastech.note.exception.NotFoundException;
import com.wastech.note.model.Category;
import com.wastech.note.model.Note;
import com.wastech.note.repository.NoteRepository;
import graphql.GraphQLException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class NoteService {

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private FileService fileService;

    public Note findById(Long id) {
        return noteRepository.findById(id)
            .orElseThrow(() -> new NotFoundException("Note not found with id: " ,id));
    }



    @Transactional
    public Note updateNoteImage(Long noteId, String imageUrl) {
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new NotFoundException("Note not found with id: ", noteId));

        // Delete old image if exists
        if (note.getImageUrl() != null) {
            fileService.deleteFile(note.getImageUrl());
        }

        note.setImageUrl(imageUrl);
        return noteRepository.save(note);
    }
    public List<Note> findByCategory(Category category) {
        return noteRepository.findByCategory(category);
    }

    public List<Note> findAllNotes() {
        return noteRepository.findAll();
    }

    @Transactional
    public Note createNote(String imageUrl, String title, String description, Category category) {
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
        Long noteId = Long.parseLong(id);
        Note note = noteRepository.findById(noteId)
            .orElseThrow(() -> new NotFoundException("Note not found with id: ", noteId));

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

    public Boolean deleteNoteById(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NotFoundException("Note not found with id: ", id);
        }
        noteRepository.deleteById(id);
        return true;
    }
}

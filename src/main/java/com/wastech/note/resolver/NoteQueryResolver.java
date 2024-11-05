package com.wastech.note.resolver;

import com.wastech.note.model.Category;
import com.wastech.note.model.Note;
import com.wastech.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class NoteQueryResolver {

    @Autowired
    private final NoteService noteService;

    public NoteQueryResolver(NoteService noteService) {
        this.noteService = noteService;
    }

    @QueryMapping

    /**
     * Retrieves a note by its ID.
     *
     * @param id the ID of the note
     * @return the note with the given ID
     */
    public Note getNoteById(@Argument Long id) {
        return noteService.findById(id);
    }

    @QueryMapping
    public List<Note> getNotesByCategory(@Argument Category category) {
        return noteService.findByCategory(category);
    }

    @QueryMapping
    public List<Note> getAllNotes() {
        return noteService.findAllNotes();
    }
}


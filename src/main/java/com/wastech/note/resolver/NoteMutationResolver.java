package com.wastech.note.resolver;

import com.wastech.note.model.Category;
import com.wastech.note.model.Note;
import com.wastech.note.service.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Controller
public class NoteMutationResolver {


    private final NoteService noteService;

    public NoteMutationResolver(NoteService noteService) {
        this.noteService = noteService;
    }

    @MutationMapping
    public Note createNote(
        @Argument String imageUrl,
        @Argument String title,
        @Argument String description,
        @Argument Category category
    ) {
        return noteService.createNote(imageUrl, title, description, category);
    }

    @MutationMapping
    public Note updateNoteImage(@Argument Long id, @Argument String imageUrl) {
        return noteService.updateNoteImage(id, imageUrl);
    }

    @MutationMapping
    public Note updateNote(
        @Argument String id,
        @Argument(name = "imageUrl") String imageUrl,
        @Argument(name = "title") String title,
        @Argument(name = "description") String description,
        @Argument(name = "category") Category category
    ) {
        return noteService.updateNote(id, imageUrl, title, description, category);
    }

    @MutationMapping
    public Boolean deleteNoteById(@Argument Long id) {
        return noteService.deleteNoteById(id);
    }
}

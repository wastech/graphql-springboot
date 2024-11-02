package com.wastech.note.repository;

import com.wastech.note.model.Category;
import com.wastech.note.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    List<Note> findByCategory(Category category);
}

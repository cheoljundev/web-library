package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;

import java.util.List;
import java.util.Optional;

public interface BookCoverRepository {
    void save(BookCover cover);
    void remove(Long coverId);
    Optional<BookCover> findById(Long coverId);
    Optional<BookCover> findByBookId(Long bookId);
    List<BookCover> findAll();
}

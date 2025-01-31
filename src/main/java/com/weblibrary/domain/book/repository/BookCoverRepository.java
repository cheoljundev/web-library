package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;

import java.util.List;

public interface BookCoverRepository {
    void save(BookCover cover);
    void remove(Long coverId);
    BookCover findById(Long coverId);
    BookCover findByBookId(Long bookId);
    List<BookCover> findAll();
}

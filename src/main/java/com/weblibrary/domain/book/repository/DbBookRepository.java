package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;

public interface DbBookRepository {
    Book update(Book book);
}

package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryBookRepository implements BookRepository {

    private final Map<Long, Book> store = new HashMap<>();

    public Long lastId = 0L;

    @Override
    public void save(String bookName, String isbn) {
        Book book = new Book(lastId++, bookName, isbn);
        store.put(book.getId(), book);
    }

    @Override
    public Book findById(Long id) {
        return store.get(id);
    }

    @Override
    public Book findByName(String name) {
        for (Book book : store.values()) {
            if (book.getName().equals(name)) {
                return book;
            }
        }

        return null;
    }

    @Override
    public Book findByIsbn(String isbn) {
        for (Book book : store.values()) {
            if (book.getIsbn().equals(isbn)) {
                return book;
            }
        }

        return null;
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Book remove(Long bookId) {
        return store.remove(bookId);
    }

    @Override
    public void clearAll() {
        store.clear();
    }
}

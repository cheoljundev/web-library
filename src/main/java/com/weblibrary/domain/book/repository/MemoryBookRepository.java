package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MemoryBookRepository implements BookRepository {

    @Getter
    private static final BookRepository instance = new MemoryBookRepository();

    private final Map<Long, Book> store = new HashMap<>();

    public Long lastId;

    @Override
    public void save(Book book) {
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
    public Book remove(Book book) {
        return store.remove(book.getId());
    }

    @Override
    public void clearAll() {
        store.clear();
    }
}

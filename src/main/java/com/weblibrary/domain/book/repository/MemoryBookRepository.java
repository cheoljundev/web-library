package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
public class MemoryBookRepository implements BookRepository {

    private final Map<Long, Book> store = new HashMap<>();
    public static Long lastId = 0L;

    @Override
    public Book save(Book book) {
        book.setBookId(incrementLastId());
        store.put(book.getBookId(), book);
        log.debug("saved book={}", book);
        return book;
    }

    private Long incrementLastId() {
        return ++lastId;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Book> findByName(String bookName) {
        return findAll().stream()
                .filter(book -> book.getBookName().equals(bookName))
                .findFirst();
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return findAll().stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst();
    }

    @Override
    public List<Book> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public Optional<Book> remove(Long bookId) {
        return Optional.ofNullable(store.remove(bookId));
    }

    public void clearAll() {
        store.clear();
    }
}

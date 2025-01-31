package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.NewBookForm;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Slf4j
@Repository
public class MemoryBookRepository implements BookRepository {

    private final Map<Long, Book> store = new HashMap<>();
    public Long lastId = 0L;

    @Override
    public void save(Book book) {
        book.setId(incrementLastId());
        store.put(book.getId(), book);
        log.debug("saved book={}", book);
    }

    private Long incrementLastId() {
        return ++lastId;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    @Override
    public Optional<Book> findByName(String name) {
        return findAll().stream()
                .filter(book -> book.getName().equals(name))
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

    @Override
    public void clearAll() {
        store.clear();
    }
}

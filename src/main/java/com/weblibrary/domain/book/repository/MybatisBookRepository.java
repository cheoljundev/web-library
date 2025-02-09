package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisBookRepository implements BookRepository {

    private final BookMapper mapper;

    @Override
    public Book save(Book book) {
        mapper.save(book);
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return mapper.findById(id);
    }

    @Override
    public Optional<Book> findByName(String name) {
        return mapper.findByName(name);
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return mapper.findByIsbn(isbn);
    }

    @Override
    public List<Book> findAll(Number limit, Number offset) {
        return mapper.findAll(limit, offset);
    }

    @Override
    public int countAll() {
        return mapper.countAll();
    }

    @Override
    public void remove(Long bookId) {
        mapper.remove(bookId);
    }

    @Override
    public void update(Book book) {
        mapper.update(book);
    }
}

package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;

    public void addBook(NewBookDto newBookDto) {
        bookRepository.save(newBookDto.getBookName(), newBookDto.getIsbn());
    }

    public void deleteBook(Long bookId) {
        bookRepository.remove(bookId).orElseThrow(NotFoundBookException::new);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findBookByName(String name) {
        return bookRepository.findByName(name);
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}

package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.repository.MemoryBookRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookService {
    @Getter
    private static final BookService instance = new BookService();

    private final BookRepository bookRepository = MemoryBookRepository.getInstance();

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public Book deleteBook(Book book) {
        return bookRepository.remove(book);
    }

    public Book findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Book findBookByName(String name) {
        return bookRepository.findByName(name);
    }

    public Book findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<Book> findAll() {
        return bookRepository.findAll();
    }
}

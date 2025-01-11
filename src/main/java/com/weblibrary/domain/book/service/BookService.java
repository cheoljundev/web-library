package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookInfo;
import com.weblibrary.domain.book.model.dto.NewBookInfo;
import com.weblibrary.domain.book.repository.BookRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public void addBook(NewBookInfo newBookDto) {
        bookRepository.save(newBookDto.getBookName(), newBookDto.getIsbn());
    }

    public Book deleteBook(Long bookId) {
        return bookRepository.remove(bookId);
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

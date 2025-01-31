package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.NewBookForm;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UploadRepository uploadRepository;

    public void addBook(NewBookForm newBookForm) throws IOException {
        UploadFile coverImage = uploadRepository.storeFile(newBookForm.getCoverImage());
        Book book = new Book(newBookForm.getBookName(), newBookForm.getIsbn(), coverImage);
        bookRepository.save(book);
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

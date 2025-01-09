package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.repository.MemoryBookRepository;
import com.weblibrary.domain.user.model.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookService {
    @Getter
    private final BookService instance = new BookService();

    private final BookRepository bookRepository = MemoryBookRepository.getInstance();

    public void addBook(Book book) {
        bookRepository.save(book);
    }

    public Book deleteBook(Book book) {
        return bookRepository.remove(book);
    }

    public Book modifyBook(Book book, Book newBook) {
        Book oldBook = new Book(
                book.getId(),
                book.getName(),
                book.getIsbn()
        );
        oldBook.setRental(book.isRental());

        book.setName(newBook.getName());
        book.setIsbn(newBook.getIsbn());
        book.setRental(newBook.isRental());
        return oldBook;
    }

    public boolean checkoutBook(User rentedBy, Book book) {
        return bookRepository.checkoutBook(rentedBy, book);
    }

    public boolean checkinBook(User rentedBy, Book book) {
        return bookRepository.checkoutBook(rentedBy, book);
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
}

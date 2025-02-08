package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.model.Book;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Test
    void save() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);

        //when
        Book savedBook = bookService.save(newBookForm);

        //then
        Optional<Book> findBook = bookService.findBookById(savedBook.getBookId());
        assertThat(findBook).isNotEmpty();

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void modify() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        ModifyBookForm modifyBookForm = new ModifyBookForm(savedBook.getBookId(), "modify", "modify12345", multipartFile);
        bookService.modify(modifyBookForm);

        //then
        Book findBook = bookService.findBookById(savedBook.getBookId()).get();
        assertThat(findBook.getBookName()).isEqualTo("modify");

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void deleteBook() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        bookService.deleteBook(savedBook.getBookId());

        //then
        Optional<Book> findBook = bookService.findBookById(savedBook.getBookId());
        assertThat(findBook).isEmpty();
    }

    @Test
    void updateBook() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        Book updateBook = new Book("update", "update12345");
        updateBook.setBookId(savedBook.getBookId());
        bookService.updateBook(updateBook);

        //then
        Book findBook = bookService.findBookById(savedBook.getBookId()).get();
        assertThat(findBook.getBookName()).isEqualTo("update");

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void findBookById() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        Optional<Book> findBook = bookService.findBookById(savedBook.getBookId());

        //then
        assertThat(findBook).isNotEmpty();

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void findBookByName() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        Optional<Book> findBook = bookService.findBookByName(savedBook.getBookName());

        //then
        assertThat(findBook).isNotEmpty();

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void findBookByIsbn() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "12345", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        Optional<Book> findBook = bookService.findBookByIsbn(savedBook.getIsbn());

        //then
        assertThat(findBook).isNotEmpty();

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void findAll() {
        //given

        List<Book> books = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
            NewBookForm newBookForm = new NewBookForm("test" + i, "12345" + i , multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        //when
        Page<BookListItem> bookPage = bookService.findAll(PageRequest.of(0, 5));

        //then
        assertThat(bookPage.getContent().size()).isEqualTo(5);
        assertThat(bookPage.getTotalElements()).isEqualTo(10);
        assertThat(bookPage.getTotalPages()).isEqualTo(2);

        //cleanUp
        for (Book book : books) {
            bookService.deleteBook(book.getBookId());
        }
    }
}
package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookSearchCond;
import com.weblibrary.web.response.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@Transactional
@SpringBootTest
class BookServiceTest {

    @Autowired
    BookService bookService;

    @Test
    void save() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);

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
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        ModifyBookForm modifyBookForm = new ModifyBookForm(savedBook.getBookId(), "modify", "modifyAuthor", "modify12345", "testDescription", multipartFile);
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
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        bookService.deleteBook(savedBook.getBookId());

        //then
        Optional<Book> findBook = bookService.findBookById(savedBook.getBookId());
        assertThat(findBook).isEmpty();
    }


    @Test
    void findBookById() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);
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
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);
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
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        Optional<Book> findBook = bookService.findBookByIsbn(savedBook.getIsbn());

        //then
        assertThat(findBook).isNotEmpty();

        //cleanUp
        bookService.deleteBook(savedBook.getBookId());
    }

    @Test
    void findBookInfoByBookId() {
        // given
        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());
        NewBookForm newBookForm = new NewBookForm("test", "testAuthor", "12345", "testDescription", multipartFile);
        Book savedBook = bookService.save(newBookForm);

        //when
        BookInfo bookInfo = bookService.findBookInfoByBookId(savedBook.getBookId()).get();

        //then
        assertThat(bookInfo).isNotNull();
        assertThat(bookInfo.getId()).isEqualTo(savedBook.getBookId());
        assertThat(bookInfo.getBookName()).isEqualTo(savedBook.getBookName());
        assertThat(bookInfo.getAuthor()).isEqualTo(savedBook.getAuthor());
        assertThat(bookInfo.getIsbn()).isEqualTo(savedBook.getIsbn());
    }

    @Test
    void findAll_no_cond() {
        //given

        List<Book> books = new ArrayList<>();

        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        for (int i = 0; i < 10; i++) {
            NewBookForm newBookForm = new NewBookForm("test" + i, "testAuthor" + i, "12345" + i, "testDescription", multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        //when
        BookSearchCond cond = new BookSearchCond();
        PageRequest pageable = PageRequest.of(0, 5);
        PageResponse<BookInfo> bookPage = bookService.findAll(cond, pageable);

        //then
        assertThat(bookPage.getContent().size()).isEqualTo(5);
        assertThat(bookPage.getTotalElements()).isEqualTo(10);
        assertThat(bookPage.getTotalPages()).isEqualTo(2);

        //cleanUp
        for (Book book : books) {
            bookService.deleteBook(book.getBookId());
        }
    }

    @Test
    void findAll_cond_bookName() {
        //given
        List<Book> books = new ArrayList<>();

        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        for (int i = 0; i < 10; i++) {
            NewBookForm newBookForm = new NewBookForm("test" + i, "testAuthor" + i, "12345" + i, "testDescription" + i, multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        for (int i = 0; i < 2; i++) {
            NewBookForm newBookForm = new NewBookForm("good newBook" + i, "testAuthor" + i, "23456" + i, "testDescription" + i, multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        //when
        BookSearchCond cond = new BookSearchCond("newBook", null, null);
        PageRequest pageable = PageRequest.of(0, 5);
        PageResponse<BookInfo> bookPage = bookService.findAll(cond, pageable);

        //then
        assertThat(bookPage.getContent().size()).isEqualTo(2);
        assertThat(bookPage.getTotalElements()).isEqualTo(2);
        assertThat(bookPage.getTotalPages()).isEqualTo(1);

        //cleanUp
        for (Book book : books) {
            bookService.deleteBook(book.getBookId());
        }
    }

    @Test
    void findAll_cond_author() {
        //given
        List<Book> books = new ArrayList<>();

        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        for (int i = 0; i < 10; i++) {
            NewBookForm newBookForm = new NewBookForm("test" + i, "testAuthor" + i, "12345" + i, "testDescription" + i, multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        for (int i = 0; i < 2; i++) {
            NewBookForm newBookForm = new NewBookForm("good newBook" + i, "김철준" + i, "23456" + i, "testDescription" + i, multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        //when
        BookSearchCond cond = new BookSearchCond(null, "김철준", null);
        PageRequest pageable = PageRequest.of(0, 5);
        PageResponse<BookInfo> bookPage = bookService.findAll(cond, pageable);

        //then
        assertThat(bookPage.getContent().size()).isEqualTo(2);
        assertThat(bookPage.getTotalElements()).isEqualTo(2);
        assertThat(bookPage.getTotalPages()).isEqualTo(1);

        //cleanUp
        for (Book book : books) {
            bookService.deleteBook(book.getBookId());
        }
    }

    @Test
    void findAll_cond_isbn() {
        //given
        List<Book> books = new ArrayList<>();

        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        for (int i = 0; i < 10; i++) {
            NewBookForm newBookForm = new NewBookForm("test" + i, "testAuthor" + i, "12345" + i, "testDescription" + i, multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        NewBookForm findBookForm = new NewBookForm("good newBook", "김철준", "23456", "testDescription", multipartFile);
        Book findBook = bookService.save(findBookForm);
        books.add(findBook);

        //when
        BookSearchCond cond = new BookSearchCond(null, null, "23456");
        PageRequest pageable = PageRequest.of(0, 5);
        PageResponse<BookInfo> bookPage = bookService.findAll(cond, pageable);

        //then
        assertThat(bookPage.getContent().size()).isEqualTo(1);
        assertThat(bookPage.getTotalElements()).isEqualTo(1);
        assertThat(bookPage.getTotalPages()).isEqualTo(1);

        //cleanUp
        for (Book book : books) {
            bookService.deleteBook(book.getBookId());
        }
    }

    @Test
    void findAll_cond_and() {
        //given
        List<Book> books = new ArrayList<>();

        MultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test data".getBytes());

        for (int i = 0; i < 10; i++) {
            NewBookForm newBookForm = new NewBookForm("test" + i, "testAuthor" + i, "12345" + i, "testDescription" + i, multipartFile);
            Book book = bookService.save(newBookForm);
            books.add(book);
        }

        NewBookForm findBookForm = new NewBookForm("good newBook", "김철준", "23456", "testDescription", multipartFile);
        Book findBook = bookService.save(findBookForm);
        books.add(findBook);

        NewBookForm likeBookForm = new NewBookForm("not good newBook", "김철준 유사 작가", "234564", "testDescription", multipartFile);
        Book likeBook = bookService.save(likeBookForm);
        books.add(likeBook);

        //when
        BookSearchCond cond = new BookSearchCond("good newBook", "김철준", "23456");
        PageRequest pageable = PageRequest.of(0, 5);
        PageResponse<BookInfo> bookPage = bookService.findAll(cond, pageable);

        //then
        assertThat(bookPage.getContent().size()).isEqualTo(1);
        assertThat(bookPage.getTotalElements()).isEqualTo(1);
        assertThat(bookPage.getTotalPages()).isEqualTo(1);

        //cleanUp
        for (Book book : books) {
            bookService.deleteBook(book.getBookId());
        }
    }
}
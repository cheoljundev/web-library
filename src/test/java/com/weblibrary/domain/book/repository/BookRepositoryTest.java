package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.NewBookForm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class BookRepositoryTest {

    @Autowired
    BookRepository bookRepository;

    @Test
    void save() {
        //given
        Book book = new Book("testBook", "testAuthor", "12345");

        //when
        Book savedBook = bookRepository.save(book);

        //then
        Book findBook = bookRepository.findById(savedBook.getBookId()).get();
        assertThat(findBook).isEqualTo(savedBook);
    }

    @Test
    void findById() {
        //given
        Book book = new Book("testBook", "testAuthor", "12345");
        Book savedBook = bookRepository.save(book);

        //when
        Book findBook = bookRepository.findById(savedBook.getBookId()).get();

        //then
        assertThat(findBook).isEqualTo(savedBook);
    }

    @Test
    void findByName() {
        //given
        Book book = new Book("testBook", "testAuthor", "12345");
        Book savedBook = bookRepository.save(book);

        //when
        Book findBook = bookRepository.findByName(savedBook.getBookName()).get();

        //then
        assertThat(findBook).isEqualTo(savedBook);
    }

    @Test
    void findByIsbn() {
        //given
        Book book = new Book("testBook", "testAuthor", "12345");
        Book savedBook = bookRepository.save(book);

        //when
        Book findBook = bookRepository.findByIsbn(savedBook.getIsbn()).get();

        //then
        assertThat(findBook).isEqualTo(savedBook);
    }

    @Test
    void findAll_no_cond() {
        //given
        for (int i = 0; i < 3; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "12345" + i);
            bookRepository.save(book);
        }
        //when
        Pageable pageable = PageRequest.of(0, 2);
        BookSearchCond cond = new BookSearchCond();
        List<Book> books = bookRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(books.size()).isEqualTo(2);
    }


    @Test
    void findAll_cond_bookName() {
        //given
        for (int i = 0; i < 20; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "1234" + i);
            bookRepository.save(book);
        }

        for (int i = 0; i < 2; i++) {
            Book book = new Book( "newBook" + i, "testAuthor1", "5678" + i);
            bookRepository.save(book);
        }

        //when
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchCond cond = new BookSearchCond("newBook", null, null);
        List<Book> books = bookRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(books.size()).isEqualTo(2);
    }


    @Test
    void findAll_cond_author() {
        //given
        for (int i = 0; i < 20; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "1234" + i);
            bookRepository.save(book);
        }

        for (int i = 0; i < 3; i++) {
            Book book = new Book( "newBook" + i, "김철준" + i, "5678" + i);
            bookRepository.save(book);
        }

        //when
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchCond cond = new BookSearchCond(null, "김철준", null);
        List<Book> books = bookRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(books.size()).isEqualTo(3);
    }
    @Test
    void findAll_cond_isbn() {
        //given
        for (int i = 0; i < 20; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "1234" + i);
            bookRepository.save(book);
        }

        Book findBook = new Book("unique book", "김철준", "94932");
        bookRepository.save(findBook);

        //when
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchCond cond = new BookSearchCond(null, null, "94932");
        List<Book> books = bookRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getBookName()).isEqualTo("unique book");
    }

    @Test
    void findAll_cond_and() {
        //given
        for (int i = 0; i < 20; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "1234" + i);
            bookRepository.save(book);
        }

        Book findBook = new Book("unique book", "김철준", "94932");
        bookRepository.save(findBook);

        Book likeBook = new Book("not unique book", "김철준 유사 작가", "949324");
        bookRepository.save(likeBook);

        //when
        Pageable pageable = PageRequest.of(0, 10);
        BookSearchCond cond = new BookSearchCond("unique book", "김철준", "94932");
        List<Book> books = bookRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(books.size()).isEqualTo(1);
        assertThat(books.get(0).getBookName()).isEqualTo("unique book");
    }

    @Test
    void countAll_no_cond() {
        //given
        for (int i = 0; i < 3; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "12345" + i);
            bookRepository.save(book);
        }

        //when
        BookSearchCond cond = new BookSearchCond();
        int total = bookRepository.countAll(cond);

        //then
        assertThat(total).isEqualTo(3);
    }

    @Test
    void countAll_cond_bookName() {
        //given
        for (int i = 0; i < 3; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "12345" + i);
            bookRepository.save(book);
        }

        for (int i = 0; i < 2; i++) {
            Book book = new Book( "newBook" + i, "testAuthor1", "5678" + i);
            bookRepository.save(book);
        }

        //when
        BookSearchCond cond = new BookSearchCond("newBook", null, null);
        int total = bookRepository.countAll(cond);

        //then
        assertThat(total).isEqualTo(2);
    }

    @Test
    void countAll_cond_author() {
        //given
        for (int i = 0; i < 3; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "12345" + i);
            bookRepository.save(book);
        }

        for (int i = 0; i < 3; i++) {
            Book book = new Book( "newBook" + i, "김철준" + i, "5678" + i);
            bookRepository.save(book);
        }

        //when
        BookSearchCond cond = new BookSearchCond(null, "김철준", null);
        int total = bookRepository.countAll(cond);

        //then
        assertThat(total).isEqualTo(3);
    }

    @Test
    void countAll_cond_isbn() {
        //given
        for (int i = 0; i < 3; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "12345" + i);
            bookRepository.save(book);
        }

        Book findBook = new Book("unique book", "김철준", "94932");
        bookRepository.save(findBook);

        //when
        BookSearchCond cond = new BookSearchCond(null, null, "94932");
        int total = bookRepository.countAll(cond);

        //then
        assertThat(total).isEqualTo(1);
    }

    @Test
    void countAll_cond_and() {
        //given
        for (int i = 0; i < 3; i++) {
            Book book = new Book("test" + i, "testAuthor" + i, "12345" + i);
            bookRepository.save(book);
        }

        Book findBook = new Book("unique book", "김철준", "94932");
        bookRepository.save(findBook);

        Book likeBook = new Book("not unique book", "김철준 유사 작가", "949324");
        bookRepository.save(likeBook);

        //when
        BookSearchCond cond = new BookSearchCond("unique book", "김철준", "94932");
        int total = bookRepository.countAll(cond);

        //then
        assertThat(total).isEqualTo(1);
    }

    @Test
    void remove() {
        //given
        Book book = new Book("testBook", "testAuthor", "12345");
        Book savedBook = bookRepository.save(book);

        //when
        bookRepository.remove(savedBook.getBookId());

        //then
        assertThat(bookRepository.findById(savedBook.getBookId())).isEmpty();
    }

    @Test
    void update() {
        //given
        Book book = new Book("testBook", "testAuthor", "12345");
        Book savedBook = bookRepository.save(book);

        //when
        Book updateBook = new Book("update", "updateAuthor", "54321");
        updateBook.setBookId(savedBook.getBookId());
        bookRepository.update(updateBook);

        //then
        Book findBook = bookRepository.findById(savedBook.getBookId()).get();
        assertThat(findBook.getBookName()).isEqualTo(updateBook.getBookName());
        assertThat(findBook.getAuthor()).isEqualTo(updateBook.getAuthor());
        assertThat(findBook.getIsbn()).isEqualTo(updateBook.getIsbn());
    }
}
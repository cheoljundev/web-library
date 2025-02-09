package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
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
    void findAll() {
        //given
        Book book1 = new Book("test1", "testAuthor1", "12345");
        Book book2 = new Book("test2", "testAuthor2", "45678");
        Book book3 = new Book("test3", "testAuthor3", "60431");

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        //when
        Pageable pageable = PageRequest.of(0, 2);
        List<Book> books = bookRepository.findAll(pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(books.size()).isEqualTo(2);
    }

    @Test
    void countAll() {
        //given
        Book book1 = new Book("test1", "testAuthor1", "12345");
        Book book2 = new Book("test2", "testAuthor2", "45678");
        Book book3 = new Book("test3", "testAuthor3", "60431");

        bookRepository.save(book1);
        bookRepository.save(book2);
        bookRepository.save(book3);

        //when
        int total = bookRepository.countAll();

        //then
        assertThat(total).isEqualTo(3);
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
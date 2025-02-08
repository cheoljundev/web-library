package com.weblibrary.domain.rental.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class BookRentalServiceTest {

    @Autowired
    BookRentalService bookRentalService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void rentBook() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "12345"));

        //when
        Rental rental = bookRentalService.rentBook(user, book);

        //then
        assertThat(rental.getUserId()).isEqualTo(user.getUserId());
        assertThat(rental.getBookId()).isEqualTo(book.getBookId());
        assertThat(rental.getRentedAt()).isNotNull();
        assertThat(rental.getReturnedAt()).isNull();
    }

    @Test
    void returnBook() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "12345"));
        bookRentalService.rentBook(user, book);

        //when
        Rental rental = bookRentalService.returnBook(user, book);

        //then
        assertThat(rental.getUserId()).isEqualTo(user.getUserId());
        assertThat(rental.getBookId()).isEqualTo(book.getBookId());
        assertThat(rental.getRentedAt()).isNotNull();
        assertThat(rental.getReturnedAt()).isNotNull();
    }

    @Test
    void findUserByBookId() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "12345"));
        bookRentalService.rentBook(user, book);

        //when
        User findUser = bookRentalService.findUserByBookId(book.getBookId());

        //then
        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());
    }
}
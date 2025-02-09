package com.weblibrary.domain.rental.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class RentalServiceTest {

    @Autowired
    RentalService rentalService;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void rentBook() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));

        //when
        Rental rental = rentalService.rentBook(user, book);

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
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        rentalService.rentBook(user, book);

        //when
        Rental rental = rentalService.returnBook(user, book);

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
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        rentalService.rentBook(user, book);

        //when
        User findUser = rentalService.findUserByBookId(book.getBookId());

        //then
        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());
    }
}
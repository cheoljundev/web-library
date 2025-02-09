package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
@Slf4j
class RentalRepositoryTest {

    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));

        //when
        Rental saved = rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));

        //then
        assertThat(saved.getUserId()).isEqualTo(user.getUserId());
        assertThat(saved.getBookId()).isEqualTo(book.getBookId());
        assertThat(saved.getRentedAt()).isNotNull();
        assertThat(saved.getReturnedAt()).isNull();
    }

    @Test
    void findActiveRentalByBookId_ok() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));

        //when
        Rental rental = rentalRepository.findActiveRentalByBookId(book.getBookId()).get();

        //then
        assertThat(rental.getUserId()).isEqualTo(user.getUserId());
    }


    @Test
    void findActiveRentalByBookId_fail() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        Rental saved = rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));
        saved.returnBook();
        rentalRepository.update(saved);

        //when
        Rental rental = rentalRepository.findActiveRentalByBookId(book.getBookId()).orElse(null);

        //then
        assertThat(rental).isNull();
    }

    @Test
    void findRentalsByUserId() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book1 = bookRepository.save(new Book("testBook1", "testAuthor1", "12345"));
        Book book2 = bookRepository.save(new Book("testBook2", "testAuthor2","45678"));
        Book book3 = bookRepository.save(new Book("testBook3", "testAuthor3","98765"));
        rentalRepository.save(new Rental(user.getUserId(), book1.getBookId()));
        rentalRepository.save(new Rental(user.getUserId(), book2.getBookId()));
        rentalRepository.save(new Rental(user.getUserId(), book3.getBookId()));

        //when
        List<Rental> rentals = rentalRepository.findRentalsByUserId(user.getUserId());

        //then
        assertThat(rentals.size()).isEqualTo(3);

    }

    @Test
    void findById() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        Rental saved = rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));

        //when
        Rental rental = rentalRepository.findById(saved.getRentalId()).get();

        //then
        assertThat(rental.getRentalId()).isEqualTo(saved.getRentalId());
    }

    @Test
    void update() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        Rental saved = rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));

        //when
        saved.returnBook();
        rentalRepository.update(saved);

        //then
        Rental rental = rentalRepository.findById(saved.getRentalId()).get();
        assertThat(rental.getReturnedAt()).isNotNull();
    }

    @Test
    void delete() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        Rental saved = rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));

        //when
        rentalRepository.remove(saved.getRentalId());

        //then
        Rental rental = rentalRepository.findById(saved.getRentalId()).orElse(null);
        assertThat(rental).isNull();
    }
}
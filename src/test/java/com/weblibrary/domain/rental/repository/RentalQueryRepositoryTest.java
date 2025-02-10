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
class RentalQueryRepositoryTest {

    @Autowired
    RentalQueryRepository rentalQueryRepository;
    @Autowired
    RentalRepository rentalRepository;
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;


    @Test
    void findActiveRentalByBookId_ok() {
        //given
        User user = userRepository.save(new User("tester", "1234"));
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));

        //when
        Rental rental = rentalQueryRepository.findActiveRentalByBookId(book.getBookId()).get();

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

        //when
        Rental rental = rentalQueryRepository.findActiveRentalByBookId(book.getBookId()).orElse(null);

        //then
        assertThat(rental).isNull();
    }

}
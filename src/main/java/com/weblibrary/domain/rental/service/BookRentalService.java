package com.weblibrary.domain.rental.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.rental.repository.BookRentalRepository;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookRentalService {
    private final BookRentalRepository bookRentalRepository;
    private final BookService bookService;
    private final UserService userService;

    public Rental rentBook(User user, Book book) {
        // remailrents가 없거나, 현재 누가 대출중인 경우 예외 발생함
        User rendtedUser = findUserByBookId(book.getBookId());

        if (user.equals(rendtedUser)) {
            throw new RentalException("이미 대출중입니다.");
        } else if (user.getRemainingRents() < 1) {
            throw new RentalException("더 이상 빌릴 수 없습니다.");
        } else if (book.isRented()) {
            throw new RentalException("이미 다른 유저가 대출중입니다.");
        }

        book.rentBook();
        user.rentBook();

        bookService.updateBook(book);
        userService.update(user);

        return bookRentalRepository.save(new Rental(book.getBookId(), user.getUserId()));
    }

    public void unRentBook(User user, Book book) {
        User rendtedUser = findUserByBookId(book.getBookId());
        if (!user.equals(rendtedUser)) {
            log.debug("rendtedUser={}", rendtedUser);
            throw new RentalException("빌리지 않은 도서입니다.");
        }

        Rental rental = bookRentalRepository.findActiveRentalByBookId(book.getBookId())
                .orElseThrow(() -> new RentalException("이미 대출중인 도서입니다."));


        book.returnBook();
        user.returnBook();
        bookService.updateBook(book);
        userService.update(user);
        bookRentalRepository.returnBook(book.getBookId());
    }

    public User findUserByBookId(Long bookId) {
        return bookRentalRepository.findActiveRentalByBookId(bookId)
                .flatMap(rental -> userService.findById(rental.getUserId()))
                .orElse(null);
    }

}

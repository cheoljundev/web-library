package com.weblibrary.domain.rental.service;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.rental.repository.BookRentalRepository;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookRentalService {
    private final BookRentalRepository bookRentalRepository;
    private final BookService bookService;
    private final UserService userService;

    /**
     * 책 대출
     *
     * @param user
     * @param book
     * @return rental
     */
    public Rental rentBook(User user, Book book) {
        // remailrents가 없거나, 현재 누가 대출중인 경우 예외 발생함
        User rendtedUser = findUserByBookId(book.getId());

        if (user.equals(rendtedUser)) {
            throw new RentalException("이미 대출중입니다.");
        } else if (user.getRemainingRents() < 1) {
            throw new RentalException("더 이상 빌릴 수 없습니다.");
        } else if (book.isRented()) {
            throw new RentalException("이미 다른 유저가 대출중입니다.");
        }

        book.rent();
        user.decrementRemainingRents();

        return bookRentalRepository.save(new Rental(book.getId(), user.getId()));
    }

    public void unRentBook(User user, Book book) {
        User rendtedUser = findUserByBookId(book.getId());
        if (!user.equals(rendtedUser)) {
            throw new RentalException("빌리지 않은 도서입니다.");
        }

        Rental rental = bookRentalRepository.findActiveRentalByBookId(book.getId())
                .orElseThrow(() -> new RentalException("이미 대출중인 도서입니다."));


        bookService.findBookById(rental.getBookId())
                .ifPresent(Book::unRent);
        userService.findById(rental.getUserId())
                .ifPresent(User::incrementRemainingRents);
        rental.returnBook();
    }

    public User findUserByBookId(Long bookId) {
        return bookRentalRepository.findActiveRentalByBookId(bookId).flatMap(rental -> userService.findById(rental.getUserId()))
                .orElse(null);
    }

}

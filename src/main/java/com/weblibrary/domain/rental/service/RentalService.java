package com.weblibrary.domain.rental.service;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookInfo;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.rental.repository.RentalQueryRepository;
import com.weblibrary.domain.rental.repository.RentalRepository;
import com.weblibrary.domain.rental.repository.RentalSearchCond;
import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserInfo;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.response.PageResponse;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class RentalService {
    private final RentalRepository rentalRepository;
    private final RentalQueryRepository rentalQueryRepository;
    private final BookService bookService;
    private final UserService userService;
    private final EntityManager em;

    public Rental rentBook(User user, Book book) {
        // remailrents가 없거나, 현재 누가 대출중인 경우 예외 발생함
        User rendtedUser = findUserByBookId(book.getBookId());
        log.debug("rendtedUser={}", rendtedUser);

        if (user.equals(rendtedUser)) {
            throw new RentalException("이미 대출중입니다.");
        } else if (user.getRemainingRents() < 1) {
            throw new RentalException("더 이상 빌릴 수 없습니다.");
        } else if (book.isRented()) {
            throw new RentalException("이미 다른 유저가 대출중입니다.");
        }

        book.rentBook();
        user.rentBook();

        return rentalRepository.save(new Rental(user.getUserId(), book.getBookId()));
    }

    public Rental returnBook(User user, Book book) {
        User rendtedUser = findUserByBookId(book.getBookId());
        if (!userService.isAdmin(user.getUserId()) && !user.equals(rendtedUser)) {
            log.debug("rendtedUser={}", rendtedUser);
            throw new RentalException("빌리지 않은 도서입니다.");
        }

        Rental rental = rentalQueryRepository.findActiveRentalByBookId(book.getBookId())
                .orElseThrow(() -> new RentalException("이미 대출중인 도서입니다."));

        book.returnBook();
        user.returnBook();
        rental.returnBook();

        return rental;
    }

    @Transactional(readOnly = true)
    public User findUserByBookId(Long bookId) {
        return rentalQueryRepository.findActiveRentalByBookId(bookId)
                .flatMap(rental -> userService.findById(rental.getUserId()))
                .orElse(null);
    }

    @Transactional(readOnly = true)
    public PageResponse<RentalInfo> findAll(RentalSearchCond cond, Pageable pageable) {
        List<Rental> rentals = rentalQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());
        long total = rentalQueryRepository.count(cond);
        List<RentalInfo> rentalInfos = rentals.stream()
                .map(rental -> {
                    UserInfo user = userService.findUserInfoById(rental.getUserId())
                            .orElseThrow(NotFoundUserException::new);
                    BookInfo book = bookService.findBookInfoByBookId(rental.getBookId())
                            .orElseThrow(NotFoundBookException::new);
                    return new RentalInfo(rental.getRentalId(), user, book, rental.getRentedAt(), rental.getReturnedAt());
                }).toList();
        PageImpl<RentalInfo> rentalPage = new PageImpl<>(rentalInfos, pageable, total);
        return new PageResponse<>(rentalInfos, rentalPage.getTotalPages(), rentalPage.getTotalElements(), rentalPage.isFirst(), rentalPage.isLast());
    }
}

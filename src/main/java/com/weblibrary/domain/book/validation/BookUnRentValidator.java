package com.weblibrary.domain.book.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.BookRentDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookUnRentValidator implements Validator {

    private final UserService userService;
    private final BookService bookService;

    @Override
    public boolean supports(Class<?> clazz) {
        return BookRentDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookRentDto bookRentDto = (BookRentDto) target;
        Long userId = bookRentDto.getUserId();
        Long bookId = bookRentDto.getBookId();

        User user = userService.findById(userId);
        Book book = bookService.findBookById(bookId);

        log.debug("in validator user={}", user);
        log.debug("in validator book={}", book);

        if (!book.isRental()) {
            errors.reject("global", "대출 상태가 아닌 도서입니다.");
        } else if (!book.getRentedBy().equals(user)) {
            errors.reject("global", "빌리지 않은 도서입니다.");
        }

    }
}

package com.weblibrary.web.rental.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.BookRentDto;
import com.weblibrary.domain.rental.repository.BookRentalRepository;
import com.weblibrary.domain.rental.service.BookRentalService;
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

    private final BookRentalService bookRentalService;
    public static final String INVALID_STATUS_FIELD = "invalid.status";
    public static final String INVALID_RENTED_FIELD = "invalid.rented";
    private final UserService userService;

    @Override
    public boolean supports(Class<?> clazz) {
        return BookRentDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookRentDto bookRentDto = (BookRentDto) target;

        User user = bookRentDto.getUser();
        Book book = bookRentDto.getBook();
//        User rentedUser = bookRentalService.findUserNameByBookId(book.getId());

        log.debug("in valudator");

        if (!book.isRented()) {
            log.debug("반납 불가, 이미 반납됨.");
            errors.rejectValue("book", INVALID_STATUS_FIELD);
//        } else if (!user.equals(rentedUser)) {rentedUser
//            errors.reject("book", INVALID_RENTED_FIELD);
        }

    }
}

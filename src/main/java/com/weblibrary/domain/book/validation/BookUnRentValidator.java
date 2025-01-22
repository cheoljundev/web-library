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

    public static final String INVALID_STATUS_FIELD = "invalid.status";
    public static final String INVALID_RENTED_FIELD = "invalid.rented";

    @Override
    public boolean supports(Class<?> clazz) {
        return BookRentDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookRentDto bookRentDto = (BookRentDto) target;

        User user = bookRentDto.getUser();
        Book book = bookRentDto.getBook();

        if (!book.isRental()) {
            errors.rejectValue("book", INVALID_STATUS_FIELD);
        } else if (!book.getRentedBy().equals(user)) {
            errors.reject("book", INVALID_RENTED_FIELD);
        }

    }
}

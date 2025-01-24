package com.weblibrary.web.book.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.BookRentDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookRentValidator implements Validator {

    public static final String INVALID_REMAIN_FIELD = "invalid.remain";
    public static final String INVALID_AVAILABLE_FIELD = "invalid.available";

    @Override
    public boolean supports(Class<?> clazz) {
        return BookRentDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        BookRentDto bookRentDto = (BookRentDto) target;

        User user = bookRentDto.getUser();
        Book book = bookRentDto.getBook();

        if (user.getRemainingRents() == 0) {
            errors.rejectValue("user", INVALID_REMAIN_FIELD, new Object[]{user.getRemainingRents()}, null);
        }
        if (book.isRental()) {
            errors.rejectValue("book", INVALID_AVAILABLE_FIELD, new Object[]{book.getRentedBy().getUsername()}, null);
        }

    }
}

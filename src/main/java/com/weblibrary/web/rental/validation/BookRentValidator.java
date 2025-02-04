package com.weblibrary.web.rental.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.dto.BookRentDto;
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
public class BookRentValidator implements Validator {

    private final BookRentalService bookRentalService;
    private final UserService userService;
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
//        if (book.isRented()) {
//            User rentedUser = bookRentalService.findUserNameByBookId(book.getId());
//            errors.rejectValue("book", INVALID_AVAILABLE_FIELD, new Object[]{rentedUser.getUsername()}, null);
//        }

    }
}

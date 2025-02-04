package com.weblibrary.web.book.validation;

import com.weblibrary.domain.book.dto.ModifyBookForm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookModifyValidator implements Validator {

    private static final String MIN_FIELD = "min";
    private static final String NOT_BLANK_FIELD = "NotBlank";
    private static final int BOOK_NAME_MIN_SIZE = 5;
    private static final int ISBN_MIN_SIZE = 5;

    @Override
    public boolean supports(Class<?> clazz) {
        return ModifyBookForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModifyBookForm book = (ModifyBookForm) target;

        String bookName = book.getBookName();
        String isbn = book.getIsbn();

        boolean isBookNameEmpty = !StringUtils.hasText(bookName);
        boolean isIsbnEmpty = !StringUtils.hasText(isbn);
        boolean isBookNameTooShort = bookName.length() < BOOK_NAME_MIN_SIZE;
        boolean isIsbnTooShort = isbn.length() < ISBN_MIN_SIZE;

        if (isBookNameEmpty || isIsbnEmpty || isBookNameTooShort || isIsbnTooShort
        ) {
            if (isBookNameEmpty) {
                errors.rejectValue("bookName", NOT_BLANK_FIELD);
            } else if (isBookNameTooShort) {
                errors.rejectValue("bookName", MIN_FIELD, new Object[]{BOOK_NAME_MIN_SIZE}, null);
            }
            if (isIsbnEmpty) {
                errors.rejectValue("isbn", NOT_BLANK_FIELD);
            } else if (isIsbnTooShort) {
                errors.rejectValue("isbn", MIN_FIELD, new Object[]{ISBN_MIN_SIZE}, null);
            }
        }

    }
}

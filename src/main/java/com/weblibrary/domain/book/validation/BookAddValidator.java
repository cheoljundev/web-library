package com.weblibrary.domain.book.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookAddValidator implements Validator {

    private final BookService bookService;
    private static final String REQUIRED_FIELD = "required";
    private static final String MIN_FIELD = "min";
    private static final String DUPLICATED_FIELD = "duplicated";
    private static final int MIN_BOOKNAME_LENGTH = 5;
    private static final int MIN_ISBN_LENGTH = 5;


    @Override
    public boolean supports(Class<?> clazz) {
        return NewBookDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewBookDto book = (NewBookDto) target;

        String bookName = book.getBookName();
        String isbn = book.getIsbn();

        boolean isBookNameEmptyOrBlank = !StringUtils.hasText(bookName);
        boolean isIsbnEmptyOrBlank = !StringUtils.hasText(isbn);

        boolean isBookNameTooShort = bookName.length() < MIN_BOOKNAME_LENGTH;
        boolean isIsbnTooShort = isbn.length() < MIN_ISBN_LENGTH;

        if (isBookNameEmptyOrBlank || isIsbnEmptyOrBlank || isBookNameTooShort || isIsbnTooShort) {
            if (isBookNameEmptyOrBlank) {
                errors.rejectValue("bookName", REQUIRED_FIELD, null);
            } else {
                if (isBookNameTooShort) {
                    errors.rejectValue("bookName", MIN_FIELD, new Object[]{MIN_BOOKNAME_LENGTH}, null);
                }
            }

            if (isIsbnEmptyOrBlank) {
                errors.rejectValue("isbn", REQUIRED_FIELD, null);
            } else if (isIsbnTooShort) {
                errors.rejectValue("isbn", MIN_FIELD, new Object[]{MIN_ISBN_LENGTH}, null);
            }
        }

        if (isDuplicated(isbn)) {
            errors.rejectValue("isbn", DUPLICATED_FIELD, null);
        }

    }

    private boolean isDuplicated(String isbn) {
        return bookService.findBookByIsbn(isbn) != null;
    }
}

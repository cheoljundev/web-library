package com.weblibrary.web.book.validation;

import com.weblibrary.domain.book.model.dto.NewBookForm;
import com.weblibrary.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookAddValidator implements Validator {

    private final BookService bookService;
    private static final String DUPLICATED_FIELD = "duplicated";
    private static final String MIN_FIELD = "min";
    private static final String NOT_BLANK_FIELD = "NotBlank";
    private static final String NOT_NULL_FIELD = "NotNull";
    private static final int BOOK_NAME_MIN_SIZE = 5;
    private static final int ISBN_MIN_SIZE = 5;

    @Override
    public boolean supports(Class<?> clazz) {
        return NewBookForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewBookForm book = (NewBookForm) target;


        String bookName = book.getBookName();
        String isbn = book.getIsbn();
        MultipartFile coverImage = book.getCoverImage();

        boolean isBookNameEmpty = !StringUtils.hasText(bookName);
        boolean isIsbnEmpty = !StringUtils.hasText(isbn);
        boolean isCoverImageEmpty = coverImage == null;
        boolean isBookNameTooShort = bookName.length() < BOOK_NAME_MIN_SIZE;
        boolean isIsbnTooShort = isbn.length() < ISBN_MIN_SIZE;

        if (isBookNameEmpty || isIsbnEmpty || isCoverImageEmpty ||
        isBookNameTooShort || isIsbnTooShort
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
            if (isCoverImageEmpty) {
                errors.rejectValue("coverImage", NOT_NULL_FIELD);
            }
            return;
        }

        if (isDuplicated(isbn)) {
            errors.rejectValue("isbn", DUPLICATED_FIELD, null);
        }

    }

    private boolean isDuplicated(String isbn) {
        return bookService.findBookByIsbn(isbn).isPresent();
    }
}

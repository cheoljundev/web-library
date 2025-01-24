package com.weblibrary.web.book.validation;

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
    private static final String DUPLICATED_FIELD = "duplicated";

    @Override
    public boolean supports(Class<?> clazz) {
        return NewBookDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        NewBookDto book = (NewBookDto) target;

        String isbn = book.getIsbn();

        if (isDuplicated(isbn)) {
            errors.rejectValue("isbn", DUPLICATED_FIELD, null);
        }

    }

    private boolean isDuplicated(String isbn) {
        return bookService.findBookByIsbn(isbn) != null;
    }
}

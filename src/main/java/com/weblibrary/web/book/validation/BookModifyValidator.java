package com.weblibrary.web.book.validation;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.dto.ModifyBookForm;
import com.weblibrary.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookModifyValidator implements Validator {

    private final BookService bookService;
    private static final String DUPLICATED_FIELD = "duplicated";
    private static final String NOT_FOUND_FIELD = "not.found";


    @Override
    public boolean supports(Class<?> clazz) {
        return ModifyBookForm.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModifyBookForm book = (ModifyBookForm) target;
        String isbn = book.getIsbn();

        bookService.findBookById(book.getId())
                .ifPresentOrElse(findOldBook -> {
                    // ISBN 중복 체크
                    if (isDuplicated(findOldBook.getIsbn(), isbn)) {
                        errors.rejectValue("isbn", DUPLICATED_FIELD, null);
                    }
                }, () -> {
                    throw new NotFoundBookException();
                });

    }

    private boolean isDuplicated(String oldIsbn, String newIsbn) {
        return !oldIsbn.equals(newIsbn) && bookService.findBookByIsbn(newIsbn) != null;
    }
}

package com.weblibrary.web.book.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
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
        return ModifyBookDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModifyBookDto book = (ModifyBookDto) target;
        Book findOldBook = bookService.findBookById(book.getId());

        if (findOldBook == null) {
            errors.rejectValue("id", NOT_FOUND_FIELD);
            return;
            // 책 못 찾으면 여기서 종료
        }

        String isbn = book.getIsbn();

        if (isDuplicated(findOldBook.getIsbn(), isbn)) {
            errors.rejectValue("isbn", DUPLICATED_FIELD, null);
        }

    }

    private boolean isDuplicated(String oldIsbn, String newIsbn) {
        return !oldIsbn.equals(newIsbn) && bookService.findBookByIsbn(newIsbn) != null;
    }
}

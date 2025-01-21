package com.weblibrary.domain.book.validation;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
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
public class BookModifyValidator implements Validator {

    private final BookService bookService;
    private static final int MIN_BOOKNAME_LENGTH = 5;
    private static final int MIN_ISBN_LENGTH = 5;


    @Override
    public boolean supports(Class<?> clazz) {
        return ModifyBookDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ModifyBookDto book = (ModifyBookDto) target;
        Book findOldBook = bookService.findBookById(book.getId());

        if (findOldBook == null) {
            errors.reject("global", "책 수정에 실패했습니다. 기존에 없는 책입니다.");
        }

        String bookName = book.getBookName();
        String isbn = book.getIsbn();

        boolean isBookNameEmptyOrBlank = !StringUtils.hasText(bookName);
        boolean isIsbnEmptyOrBlank = !StringUtils.hasText(isbn);

        boolean isBookNameTooShort = bookName.length() < MIN_BOOKNAME_LENGTH;
        boolean isIsbnTooShort = isbn.length() < MIN_ISBN_LENGTH;

        if (isBookNameEmptyOrBlank || isIsbnEmptyOrBlank || isBookNameTooShort || isIsbnTooShort) {
            if (isBookNameEmptyOrBlank) {
                errors.rejectValue("bookName", null, "책 이름은 필수 값입니다.");
            } else {
                if (isNameToShortAndNotSame(isBookNameTooShort, findOldBook, bookName)) {
                    errors.rejectValue("bookName", null, "최소 " + MIN_BOOKNAME_LENGTH + "자 이상 입력하세요.");
                }
            }

            if (isIsbnEmptyOrBlank) {
                errors.rejectValue("isbn", null, "isbn은 필수 값입니다.");
            } else if (isIsbnToShortAndNotSame(isIsbnTooShort, findOldBook, isbn)) {
                errors.rejectValue("isbn", null, null, "최소 " + MIN_ISBN_LENGTH + "자 이상 입력하세요.");
            }
        }

        if (isDuplicated(findOldBook.getIsbn(), isbn)) {
            errors.reject("global", "책 수정에 실패했습니다. 이미 등록된 책 isbn 입니다.");
        }

    }

    private static boolean isNameToShortAndNotSame(boolean isBookNameTooShort, Book findOldBook, String bookName) {
        return isBookNameTooShort && !findOldBook.getName().equals(bookName);
    }

    private static boolean isIsbnToShortAndNotSame(boolean isIsbnTooShort, Book findOldBook, String isbn) {
        return isIsbnTooShort && !findOldBook.getIsbn().equals(isbn);
    }

    private boolean isDuplicated(String oldIsbn, String newIsbn) {
        return !oldIsbn.equals(newIsbn) && bookService.findBookByIsbn(newIsbn) != null;
    }
}

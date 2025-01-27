package com.weblibrary.web.user.controller;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.BookRentDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.web.book.validation.BookRentValidator;
import com.weblibrary.web.book.validation.BookUnRentValidator;
import com.weblibrary.web.response.JsonResponse;
import com.weblibrary.web.validation.ValidationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

/**
 * 도서 대출 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class UserBookController {
    private final BookService bookService;
    private final BookRentValidator bookRentValidator;
    private final BookUnRentValidator bookUnRentValidator;
    private final ValidationUtils validationUtils;

    @PostMapping("/{bookId}/rent")
    public ResponseEntity<JsonResponse> rent(@SessionAttribute(name = "user", required = false) User user, @PathVariable("bookId") Long bookId) {

        // Optional로 Book을 안전하게 처리
        Book findBook = bookService.findBookById(bookId)
                .orElseThrow(NotFoundBookException::new);

        log.debug("rent by user={}", user);
        log.debug("rent findBook={}", findBook);

        BookRentDto bookRentDto = new BookRentDto(user, findBook);
        BindingResult bindingResult = new BeanPropertyBindingResult(bookRentDto, "rentBookDto");

        log.debug("bindingResult.getObjectName()={}", bindingResult.getObjectName());
        log.debug("bindingResult.getTarget()={}", bindingResult.getTarget());

        bookRentValidator.validate(bookRentDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return validationUtils.handleValidationErrors(bindingResult);
        }

        user.rent(findBook);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 대출되었습니다.")
                .build(), HttpStatus.OK);
    }

    @PostMapping("/{bookId}/unrent")
    public ResponseEntity<JsonResponse> unRent(@SessionAttribute(name = "user", required = false) User user, @PathVariable("bookId") Long bookId) {

        Book findBook = bookService.findBookById(bookId)
                .orElseThrow(NotFoundBookException::new);

        log.debug("unRent by user={}", user);
        log.debug("unRent findBook={}", findBook);

        BookRentDto bookRentDto = new BookRentDto(user, findBook);
        BindingResult bindingResult = new BeanPropertyBindingResult(bookRentDto, "rentBookDto");

        log.debug("bindingResult.getObjectName()={}", bindingResult.getObjectName());
        log.debug("bindingResult.getTarget()={}", bindingResult.getTarget());

        bookUnRentValidator.validate(bookRentDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return validationUtils.handleValidationErrors(bindingResult);
        }

        user.unRent(findBook);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 반납되었습니다.")
                .build(), HttpStatus.OK);

    }

}

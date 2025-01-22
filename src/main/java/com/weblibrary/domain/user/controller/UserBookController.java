package com.weblibrary.domain.user.controller;

import com.weblibrary.core.dto.response.ErrorResponse;
import com.weblibrary.core.dto.response.JsonResponse;
import com.weblibrary.core.validation.ValidationUtils;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.BookRentDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.book.validation.BookRentValidator;
import com.weblibrary.domain.book.validation.BookUnRentValidator;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<JsonResponse> rent(HttpSession session, @PathVariable("bookId") Long bookId) {
        User user = (User) session.getAttribute("user");


        if (user == null) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .message("로그인 해주세요.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        Book findBook = bookService.findBookById(bookId);

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
    public ResponseEntity<JsonResponse> unRent(HttpSession session, @PathVariable("bookId") Long bookId) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .message("로그인 해주세요.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        Book findBook = bookService.findBookById(bookId);

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

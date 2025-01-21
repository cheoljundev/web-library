package com.weblibrary.domain.user.controller;

import com.weblibrary.core.dto.response.ErrorResponse;
import com.weblibrary.core.dto.response.JsonResponse;
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
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        BookRentDto bookRentDto = new BookRentDto(user.getId(), bookId);
        BindingResult bindingResult = new BeanPropertyBindingResult(bookRentDto, "bookRentDto");

        bookRentValidator.validate(bookRentDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return handleValidationErrors(bindingResult);
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

        BookRentDto bookRentDto = new BookRentDto(user.getId(), bookId);
        BindingResult bindingResult = new BeanPropertyBindingResult(bookRentDto, "bookRentDto");

        bookUnRentValidator.validate(bookRentDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return handleValidationErrors(bindingResult);
        }

        user.unRent(findBook);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 반납되었습니다.")
                .build(), HttpStatus.OK);

    }

    private static ResponseEntity<JsonResponse> handleValidationErrors(Errors bindingResult) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();

        for (ObjectError globalError : globalErrors) {
            errors.put(globalError.getCode(), globalError.getDefaultMessage());
        }
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(ErrorResponse.builder()
                .code("validation")
                .message("validation 실패")
                .errors(errors).build()
                , HttpStatus.BAD_REQUEST);
    }

}

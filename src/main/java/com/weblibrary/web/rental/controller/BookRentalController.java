package com.weblibrary.web.rental.controller;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.service.RentalService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.web.argumentresolver.Login;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 도서 대출 컨트롤러
 */
@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookRentalController {
    private final BookService bookService;
    private final RentalService rentalService;
    private final ErrorResponseUtils errorResponseUtils;

    @PostMapping("/{bookId}/rent")
    public ResponseEntity<JsonResponse> rentBook(@Login User user, @PathVariable("bookId") Long bookId) {

        // Optional로 Book을 안전하게 처리
        Book findBook = bookService.findBookById(bookId)
                .orElseThrow(NotFoundBookException::new);

        log.debug("rent by user={}", user);
        log.debug("rent findBook={}", findBook);

        rentalService.rentBook(user, findBook);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 대출되었습니다.")
                .build());
    }

    @PostMapping("/{bookId}/return")
    public ResponseEntity<JsonResponse> returnBook(@Login User user, @PathVariable("bookId") Long bookId) {

        Book findBook = bookService.findBookById(bookId)
                .orElseThrow(NotFoundBookException::new);

        log.debug("return by user={}", user);
        log.debug("return findBook={}", findBook);

        rentalService.returnBook(user, findBook);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 반납되었습니다.")
                .build());

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundBookError(RentalException e) {
        return errorResponseUtils.handleExceptionError(e, "book");
    }

}

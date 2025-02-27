package com.weblibrary.web.rental.controller;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.service.RentalService;
import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.account.controller.LoginUser;
import com.weblibrary.web.argumentresolver.Login;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * BookRentalController는 도서 대출 및 반납을 처리하는 컨트롤러입니다.
 */
@Tag(name = "Book Rental API", description = "도서 대출 및 반납 API")
@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookRentalController {
    private final BookService bookService;
    private final RentalService rentalService;
    private final ErrorResponseUtils errorResponseUtils;
    private final UserService userService;

    /**
     * 사용자가 책을 대출합니다.
     *
     * @param loginUser 로그인한 사용자 정보
     * @param bookId 대출할 책의 ID
     * @return JsonResponse 대출 성공 메시지
     */
    @Operation(summary = "책 대출", description = "사용자가 책을 대출합니다.")
    @PostMapping("/{bookId}/rent")
    public ResponseEntity<JsonResponse> rentBook(@Login LoginUser loginUser, @PathVariable("bookId") Long bookId) {

        // Optional로 Book을 안전하게 처리
        Book findBook = bookService.findBookById(bookId)
                .orElseThrow(NotFoundBookException::new);

        log.debug("userInfo={}", loginUser);

        User user = userService.findById(loginUser.getUserId())
                .orElseThrow(NotFoundUserException::new);

        log.debug("rent by user={}", user);
        log.debug("rent findBook={}", findBook);

        rentalService.rentBook(user, findBook);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 대출되었습니다.")
                .build());
    }

    /**
     * 사용자가 책을 반납합니다.
     *
     * @param loginUser 로그인한 사용자 정보
     * @param bookId 반납할 책의 ID
     * @return JsonResponse 반납 성공 메시지
     */
    @Operation(summary = "책 반납", description = "사용자가 책을 반납합니다.")
    @PostMapping("/{bookId}/return")
    public ResponseEntity<JsonResponse> returnBook(@Login LoginUser loginUser, @PathVariable("bookId") Long bookId) {

        Book findBook = bookService.findBookById(bookId)
                .orElseThrow(NotFoundBookException::new);

        User user = userService.findById(loginUser.getUserId())
                .orElseThrow(NotFoundUserException::new);


        log.debug("return by user={}", user);
        log.debug("return findBook={}", findBook);

        rentalService.returnBook(user, findBook);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 반납되었습니다.")
                .build());

    }

    /**
     * 대출 예외 처리
     *
     * @param e 대출 예외
     * @return ErrorResponse 대출 예외 응답
     */
    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundBookError(RentalException e) {
        return errorResponseUtils.handleExceptionError(e, "bookRental");
    }

}

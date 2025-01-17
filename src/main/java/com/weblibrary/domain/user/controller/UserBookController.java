package com.weblibrary.domain.user.controller;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping("/{bookId}/rent")
    public ResponseEntity<String> rent(HttpSession session, @PathVariable("bookId") Long bookId) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ResponseEntity<>("로그인해주세요.", HttpStatus.FORBIDDEN);
        }

        Book findBook = bookService.findBookById(bookId);

        log.debug("rent by user={}", user);
        log.debug("rent findBook={}", findBook);

        if (user.rent(findBook)) {
            return new ResponseEntity<>("정상 대출 되었습니다.", HttpStatus.OK);

        }

        return new ResponseEntity<>("정상 대출 되지 않았습니다.", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/{bookId}/unrent")
    public ResponseEntity<String> unRent(HttpSession session, @PathVariable("bookId") Long bookId) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return new ResponseEntity<>("로그인해주세요.", HttpStatus.FORBIDDEN);
        }

        Book findBook = bookService.findBookById(bookId);

        log.debug("unRent by user={}", user);
        log.debug("unRent findBook={}", findBook);

        if (user.unRent(findBook)) {
            return new ResponseEntity<>("정상 반납 되었습니다.", HttpStatus.OK);
        }

        return new ResponseEntity<>("정상 반납 되지 않았습니다.", HttpStatus.BAD_REQUEST);

    }

}

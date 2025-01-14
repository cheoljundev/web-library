package com.weblibrary.domain.user.controller;

import com.weblibrary.core.controller.dto.response.RentResponse;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 도서 대출 컨트롤러
 */
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class UserBookController {
    private final BookService bookService;

    @PostMapping("/{bookId}/rent")
    public RentResponse rent(HttpSession session, @PathVariable("bookId") Long bookId, HttpServletResponse response) {
        User user = (User) session.getAttribute("user");
        Book findBook = bookService.findBookById(bookId);

        if (user.rent(findBook)) {
            return new RentResponse(
                    HttpServletResponse.SC_OK,
                    "정상적으로 대출되었습니다."
            );
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return new RentResponse(
                HttpServletResponse.SC_BAD_REQUEST,
                "대출되지 않았습니다."
        );
    }

    @PostMapping("/{bookId}/unrent")
    public RentResponse unRent(HttpSession session, @PathVariable("bookId") Long bookId, HttpServletResponse response) {
        User user = (User) session.getAttribute("user");
        Book findBook = bookService.findBookById(bookId);

        if (user.unRent(findBook)) {
            return new RentResponse(
                    HttpServletResponse.SC_OK,
                    "정상적으로 반납되었습니다."
            );
        }

        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        return new RentResponse(
                HttpServletResponse.SC_BAD_REQUEST,
                "반납되지 않았습니다."
        );
    }

}

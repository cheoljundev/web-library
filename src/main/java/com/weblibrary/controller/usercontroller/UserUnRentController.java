package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.JsonResponseController;
import com.weblibrary.controller.response.JsonResponse;
import com.weblibrary.controller.response.RentResponse;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.util.StreamUtils;

import java.io.IOException;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * 도서 반납 컨트롤러
 */
public class UserUnRentController implements JsonResponseController {
    private final BookService bookService = BookService.getInstance();

    @Override
    public JsonResponse response(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();

        // 유저 세션 획득
        User user = (User) session.getAttribute("user");

        // Book 정보 얻어서 Book 찾기
        ServletInputStream inputStream = request.getInputStream();
        Long bookId = Long.parseLong(StreamUtils.copyToString(inputStream, UTF_8));
        Book rentBook = bookService.findBookById(bookId);

        // unRent 메서드가 true이면 반납 완료
        if (user.unRent(rentBook)) {
            return new RentResponse(
                    HttpServletResponse.SC_OK,
                    "정상적으로 반납되었습니다."
            );
        }

        // 아닐 경우 반납 불가능
        return new RentResponse(
                HttpServletResponse.SC_BAD_REQUEST,
                "반납되지 않았습니다."
        );
    }
}

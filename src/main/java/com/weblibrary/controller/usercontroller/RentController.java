package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.JsonResponseController;
import com.weblibrary.controller.dto.response.JsonResponse;
import com.weblibrary.controller.dto.response.RentResponse;
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
 * 도서 대출 컨트롤러
 */
public class RentController implements JsonResponseController {
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

        // rent 메서드가 true이면 대출 완료
        if (user.rent(rentBook)) {
            return new RentResponse(
                    HttpServletResponse.SC_OK,
                    "정상적으로 대출되었습니다."
            );
        }

        // 아닐 경우 대출 불가능
        return new RentResponse(
                HttpServletResponse.SC_BAD_REQUEST,
                "대출되지 않았습니다."
        );
    }
}

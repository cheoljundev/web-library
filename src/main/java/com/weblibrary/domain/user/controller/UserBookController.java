package com.weblibrary.domain.user.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.JsonResponseController;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.core.controller.dto.response.RentResponse;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;

/**
 * 도서 대출 컨트롤러
 */
public class UserBookController implements JsonResponseController {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final BookService bookService = appConfig.bookService();

    @Override
    public JsonResponse response(HttpServletRequest request) throws IOException {
        HttpSession session = request.getSession();

        // 유저 세션 획득
        User user = (User) session.getAttribute("user");

        String[] uriParts = cleanUrlParts(request.getRequestURI());

        // Book 정보 얻어서 Book 찾기
        Long bookId = Long.parseLong(uriParts[2]);
        Book findBook = bookService.findBookById(bookId);

        switch (uriParts[3]) {
            case "rent" -> {
                return getRentResponse(user, findBook);
            }
            case "unrent" -> {
                return getUnRentResponse(user, findBook);
            }
        }

        return null;
    }

    private static RentResponse getRentResponse(User user, Book findBook) {
        // rent 메서드가 true이면 대출 완료
        if (user.rent(findBook)) {
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

    private static RentResponse getUnRentResponse(User user, Book findBook) {
        // unRent 메서드가 true이면 반납 완료
        if (user.unRent(findBook)) {
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

    private static String[] cleanUrlParts(String uri) {
        String[] uriParts = uri.split("/");
        //빈 문자열은 제거
        uriParts = Arrays.copyOfRange(uriParts, 1, uriParts.length);
        return uriParts;
    }
}

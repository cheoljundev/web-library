package com.weblibrary.domain.admin.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.JsonResponseController;
import com.weblibrary.core.controller.RedirectController;
import com.weblibrary.core.controller.dto.response.ErrorResponse;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

public class AdminBookController implements RedirectController, JsonResponseController {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();

    @Override
    public String process(HttpServletRequest request, Map<String, String> paramMap) throws IOException {

        if (paramMap.isEmpty()) {
            return null;
        }

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (!adminService.isAdmin(user.getId())) {
            return "/site/access-denied";
        }

        String uri = request.getRequestURI();

        if (uri.equals("/site/books/add")) {
            String bookName = paramMap.get("bookName");
            String isbn = paramMap.get("isbn");
            adminService.addBook(bookName, isbn);

            return "/site/admin";
        }

        return null;

    }

    @Override
    public JsonResponse response(HttpServletRequest request) throws IOException {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if (method.equals("DELETE")) {
            String[] urlParts = cleanUrlParts(uri);
            long bookId = Long.parseLong(urlParts[2]);
            Book removed = adminService.deleteBook(bookId);

            if (removed == null) {
                return new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, "삭제되지 않았습니다.");
            }

            return new JsonResponse(HttpServletResponse.SC_OK, "정상 삭제되었습니다.");
        }

        return null;
    }

    private static String[] cleanUrlParts(String uri) {
        String[] uriParts = uri.split("/");
        //빈 문자열은 제거
        uriParts = Arrays.copyOfRange(uriParts, 1, uriParts.length);
        return uriParts;
    }

}

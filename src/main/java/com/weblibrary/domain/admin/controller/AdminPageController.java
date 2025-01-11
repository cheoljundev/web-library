package com.weblibrary.domain.admin.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.ForwardController;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

public class AdminPageController implements ForwardController {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();
    private final BookService bookService = appConfig.bookService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            if (adminService.isAdmin(user.getId())) {
                setUsersInRequest(request);
                setBooksInRequest(request);
                return "admin/index";
            }
        }
        return "access-denied";
    }

    private void setUsersInRequest(HttpServletRequest request) {
        List<User> users = adminService.findAllUsers();
        request.setAttribute("users", users);
    }

    private void setBooksInRequest(HttpServletRequest request) {
        List<Book> books = bookService.findAll();
        request.setAttribute("books", books);
    }
}

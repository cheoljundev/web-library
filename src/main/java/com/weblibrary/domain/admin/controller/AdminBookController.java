package com.weblibrary.domain.admin.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.core.controller.RedirectController;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Map;

public class AdminBookController implements RedirectController {

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

        String bookName = paramMap.get("bookName");
        String isbn = paramMap.get("isbn");

        adminService.addBook(bookName, isbn);
        return "/site";
    }

}

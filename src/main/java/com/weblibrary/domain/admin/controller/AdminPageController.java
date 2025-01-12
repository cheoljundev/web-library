package com.weblibrary.domain.admin.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();
    private final BookService bookService = appConfig.bookService();

    @GetMapping("/admin")
    public String adminPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            if (adminService.isAdmin(user.getId())) {
                model.addAttribute("users", adminService.findAllUsers());
                model.addAttribute("books", bookService.findAll());
                return "admin/index";
            }
        }
        return "access-denied";
    }
}

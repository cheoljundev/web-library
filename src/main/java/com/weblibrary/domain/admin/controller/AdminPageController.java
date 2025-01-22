package com.weblibrary.domain.admin.controller;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPageController {
    private final AdminUtils adminUtils;

    @GetMapping("/admin")
    public String adminPage(HttpSession session) {
        if (adminUtils.isDefault(session)) {
            return "redirect:/access-denied";
        }

        return "admin/index";
    }
}

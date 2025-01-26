package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPageController {
    private final AdminUtils adminUtils;

    @GetMapping("/admin")
    public String adminPage(@SessionAttribute(name = "user", required = false) User user) {
        if (adminUtils.isDefault(user)) {
            return "redirect:/access-denied";
        }

        return "admin/index";
    }
}

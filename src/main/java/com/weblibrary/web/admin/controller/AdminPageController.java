package com.weblibrary.web.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminPageController {
    @GetMapping("/admin")
    public String adminPage() {
        return "redirect:/admin/book";
    }
}

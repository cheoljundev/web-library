package com.weblibrary.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@Controller
public class AccessDeniedController {

    @GetMapping("/access-denied")
    public String accessDenied(@RequestParam(name = "redirectUrl", defaultValue = "/") String redirectUrl, Model model) {
        model.addAttribute("redirectUrl", redirectUrl);
        return "error/403";
    }

}

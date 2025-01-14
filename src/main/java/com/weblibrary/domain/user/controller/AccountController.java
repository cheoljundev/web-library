package com.weblibrary.domain.user.controller;

import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * 유저 회원가입 컨트롤러, GET, POST에 따라 다르게 동작.
 */
@Controller
@RequiredArgsConstructor
public class AccountController {

    private final UserService userService;

    /* join form 보여주기 */
    @GetMapping("/join")
    public String joinForm() {
        return "home/join";
    }

    /* 회원가입 처리하기 */
    @PostMapping("/join")
    public void join(@RequestParam("username") String username, @RequestParam("password") String password, HttpServletResponse response) throws IOException {

        // Service 계층의 join메서드로 가입 처리
        userService.join(username, password);

        // 회원가입 후에 홈으로 리다이렉트
        response.sendRedirect("/");
    }

    /* 로그인 처리하기 */
    @PostMapping("/login")
    public void login(HttpSession session, HttpServletResponse response, @RequestParam("username") String username, @RequestParam("password") String password) throws IOException {

        User loginUser = userService.login(username, password);

        if (loginUser != null) {
            session.setAttribute("user", loginUser);
        }

        // 로그인 후에 홈으로 리다이렉트
        response.sendRedirect("/");

    }
}

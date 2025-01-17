package com.weblibrary.domain.user.controller;

import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@Slf4j
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

    @GetMapping("/login")
    public String loginForm() {
        return "home/login";
    }

    /* 로그인 처리하기 */
    @PostMapping("/login")
    public String login(HttpSession session, @RequestParam("username") String username, @RequestParam("password") String password) throws IOException {

        User loginUser = userService.login(username, password);

        /* 로그린한 유저 로그 찍기 */
        log.debug("loginUser={}", loginUser);

        if (loginUser != null) {
            session.setAttribute("user", loginUser);
        }

        // 로그인 후에 홈으로 리다이렉트
        return "redirect:/";

    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpSession session) {

        User user = (User) session.getAttribute("user");

        log.debug("login user={}", user);

        if (user == null) {
            return new ResponseEntity<>("로그인되지 않았습니다.", HttpStatus.FORBIDDEN);
        }

        session.setAttribute("user", null);

        return new ResponseEntity<>("로그아웃 되었습니다.", HttpStatus.OK);
    }

}

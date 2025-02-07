package com.weblibrary.web.account.controller;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.account.service.LoginUserForm;
import com.weblibrary.domain.account.exception.InvalidJoinException;
import com.weblibrary.domain.account.exception.InvalidLoginException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.web.argumentresolver.Login;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 유저 회원가입 컨트롤러, GET, POST에 따라 다르게 동작.
 */
@Controller
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;

    /* join form 보여주기 */
    @GetMapping("/join")
    public String joinForm(Model model) {
        model.addAttribute("user", new LoginUserForm());
        return "home/join";
    }

    /* 회원가입 처리하기 */
    @PostMapping("/join")
    public String join(@Validated @ModelAttribute("user") JoinUserForm form, BindingResult bindingResult) {

        log.debug("Input User DTO: {}", form);

        /* 검증에 에러가 발견되면, 폼을 보여줌. */
        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return "home/join";
        }

        /* 검증이 끝나면, 컨트롤러에서 회원가입 처리 */
        accountService.join(form);

        // 회원가입 후에 홈으로 리다이렉트
        return "redirect:/";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new LoginUserForm());
        return "home/login";
    }

    /* 로그인 처리하기 */
    @PostMapping("/login")
    public String login(HttpSession session, @Validated @ModelAttribute("user") LoginUserForm form,
                        BindingResult bindingResult,
                        @RequestParam(value = "redirectUrl", defaultValue = "/") String redirectUrl) {

        log.debug("Input User DTO: {}", form);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return "home/login";
        }

        accountService.login(session, form);

        /* 로그인 후에 redirectUrl로 리다이렉트 */
        return "redirect:" + redirectUrl;

    }

    @PostMapping("/signout")
    public ResponseEntity<JsonResponse> signOut(HttpServletRequest request, @Login User user) {
        HttpSession session = request.getSession(false);

        log.debug("login user={}", user);

        if (user == null) {
            return new ResponseEntity<>(
                    ErrorResponse.builder()
                            .message("로그인되지 않았습니다.")
                            .build()
                    , HttpStatus.BAD_REQUEST);
        }

        session.invalidate();

        return new ResponseEntity<>(JsonResponse.builder()
                .message("로그아웃 되었습니다.")
                .build(), HttpStatus.OK);
    }

    @ExceptionHandler(InvalidLoginException.class)
    public String handleInvalidLoginException(InvalidLoginException e, Model model) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        model.addAttribute("user", e.getForm());
        model.addAttribute("errors", errors);
        return "home/login";
    }

    @ExceptionHandler(InvalidJoinException.class)
    public String handleInvalidJoinException(InvalidJoinException e, Model model) {
        List<String> errors = new ArrayList<>();
        errors.add(e.getMessage());
        model.addAttribute("user", e.getForm());
        model.addAttribute("errors", errors);
        return "home/join";
    }

}

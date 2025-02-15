package com.weblibrary.web.account.controller;

import com.weblibrary.domain.account.exception.InvalidJoinException;
import com.weblibrary.domain.account.exception.InvalidLoginException;
import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.account.service.LoginUserForm;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.argumentresolver.Login;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AccountController는 다음과 같은 사용자 계정 관련 작업을 처리합니다
 * 로그인, 회원가입, 로그아웃
 * 로그인 실패, 회원가입 실패 시 ExceptionHandler를 통해 처리
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService accountService;
    private final UserService userService;
    private final ErrorResponseUtils errorResponseUtils;

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

    /**
     * 사용자 로그인 처리
     *
     * @param session       HTTP 세션
     * @param form          로그인 폼 데이터
     * @param bindingResult 검증 결과
     * @return 로그인 결과를 포함하는 ResponseEntity
     * @throws MethodArgumentNotValidException 메서드 인자가 유효하지 않은 경우
     */
    @PostMapping("/login")
    public ResponseEntity<JsonResponse> login(HttpSession session, @Validated @RequestBody LoginUserForm form,
                                              BindingResult bindingResult) throws MethodArgumentNotValidException {

        log.debug("Input User DTO: {}", form);

        if (bindingResult.hasErrors()) {
            return errorResponseUtils.handleValidationErrors(bindingResult);
        }

        accountService.login(session, form);

        /* 로그인 후에 redirectUrl로 리다이렉트 */
        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("로그인 되었습니다.")
                .build());

    }

    @PostMapping("/signout")
    public ResponseEntity<JsonResponse> signOut(HttpServletRequest request, @Login LoginUser loginUser) {
        HttpSession session = request.getSession(false);

        User user = userService.findById(loginUser.getUserId()).orElse(null);

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

    /**
     * 로그인 실패 예외 처리
     *
     * @param e InvalidLoginException 예외 객체
     * @return 에러 응답을 포함하는 ResponseEntity
     */
    @ExceptionHandler(InvalidLoginException.class)
    public ResponseEntity<ErrorResponse> handleInvalidLoginException(InvalidLoginException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("root", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("login")
                .message("로그인 실패")
                .errors(errors).build());
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

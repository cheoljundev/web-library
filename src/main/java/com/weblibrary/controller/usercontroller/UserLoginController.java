package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.UserController;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 유저 로그인 컨트롤러, GET, POST에 따라 다르게 동작.
 */
public class UserLoginController implements UserController {

    private final UserService userService = UserService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) throws IOException {

        /* 현재는 로그인은 메인에서 하므로 POST만 처리한다. */
        /* Service 계층의 join메서드로 로그인 처리 */
        userService.login(request, paramMap);

        /* 로그인 후 실패든 성공이든 홈으로 redirect */
        response.sendRedirect("/site");
        return null;
    }
}

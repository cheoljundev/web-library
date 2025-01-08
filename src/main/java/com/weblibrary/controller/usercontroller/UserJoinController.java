package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.UserController;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 유저 회원가입 컨트롤러, GET, POST에 따라 다르게 동작.
 */
public class UserJoinController implements UserController {

    private final UserService userService = UserService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        if (request.getMethod().equals("POST")) {

            // Service 계층의 join메서드로 가입 처리
            userService.join(paramMap);

            response.sendRedirect("/site");
            return null;
        }

        // GET인 경우 join jsp를 forward
        return "join";

    }
}

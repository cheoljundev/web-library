package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.UserForwardController;
import com.weblibrary.controller.UserRedirectController;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

/**
 * 유저 회원가입 컨트롤러, GET, POST에 따라 다르게 동작.
 */
public class UserJoinController implements UserForwardController, UserRedirectController {

    private final UserService userService = UserService.getInstance();

    /* GET 처리를 위한 forwardController의 process */
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        if (request.getMethod().equals("GET")) {
            // GET인 경우 join jsp를 forward
            return "join";
        }

        return null;
    }

    /* GET 처리를 위한 redirectController의 process */
    @Override
    public String process(HttpServletRequest request, Map<String, String> paramMap) {
        if (request.getMethod().equals("POST")) {

            // Service 계층의 join메서드로 가입 처리
            String username = paramMap.get("username");
            String password = paramMap.get("password");
            userService.join(username, password);

            // 로그인 후에 홈으로 리다이렉트
            return "/site";
        }

        return null;
    }
}

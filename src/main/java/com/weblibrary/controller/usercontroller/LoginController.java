package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.RedirectController;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.Map;

/**
 * 유저 로그인 컨트롤러, GET, POST에 따라 다르게 동작.
 */
public class LoginController implements RedirectController {

    private final UserService userService = UserService.getInstance();

    @Override
    public String process(HttpServletRequest request, Map<String, String> paramMap) {

        /* 현재는 로그인은 메인에서 하므로 POST만 처리한다. */
        /* Service 계층의 join메서드로 로그인 처리 */
        String username = paramMap.get("username");
        String password = paramMap.get("password");

        User loginUser = userService.login(username, password);

        if (loginUser != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", loginUser);
        }

        /* 로그인 후 실패든 성공이든 홈으로 redirect */
        return "/site";
    }
}

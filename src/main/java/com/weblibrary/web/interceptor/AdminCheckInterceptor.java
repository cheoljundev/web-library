package com.weblibrary.web.interceptor;

import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.SessionConst;
import com.weblibrary.web.account.controller.LoginUser;
import com.weblibrary.web.exception.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@RequiredArgsConstructor
public class AdminCheckInterceptor implements HandlerInterceptor {
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession(false);

        if (session == null || session.getAttribute(SessionConst.LOGIN_USER) == null) {
            throw new UnauthorizedAccessException("로그인해주세요.");
        }

        LoginUser user = (LoginUser) session.getAttribute(SessionConst.LOGIN_USER);

        if (!userService.isAdmin(user.getUserId())) {
            session.invalidate();
            throw new UnauthorizedAccessException("관리자가 아닙니다.");
        }

        return true;
    }
}

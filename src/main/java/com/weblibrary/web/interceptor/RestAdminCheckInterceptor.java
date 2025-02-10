package com.weblibrary.web.interceptor;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.web.SessionConst;
import com.weblibrary.web.account.controller.LoginUser;
import com.weblibrary.web.exception.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class RestAdminCheckInterceptor implements HandlerInterceptor {
    private final AdminService adminService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (request.getMethod().equals("GET")) {
            return true;
        }

        HttpSession session = request.getSession(false);
        LoginUser user = (LoginUser) session.getAttribute(SessionConst.LOGIN_USER);
        if (!adminService.isAdmin(user.getUserId())) {
                session.invalidate();
                throw new UnauthorizedAccessException("관리자가 아닙니다.");
            }
        return true;
    }
}

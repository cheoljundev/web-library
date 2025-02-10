package com.weblibrary.web.interceptor;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.web.SessionConst;
import com.weblibrary.web.account.controller.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.servlet.HandlerInterceptor;

@RequiredArgsConstructor
public class SsrAdminCheckInterceptor implements HandlerInterceptor {
    private final AdminService adminService;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        LoginUser user = (LoginUser) session.getAttribute(SessionConst.LOGIN_USER);
        String requestURI = request.getRequestURI();
        if (!adminService.isAdmin(user.getUserId())) {
                session.invalidate();
                response.sendRedirect("/access-denied?redirectUrl=" + requestURI);
                return false;
            }
        return true;
    }
}

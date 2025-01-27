package com.weblibrary.web.interceptor;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.web.exception.UnauthorizedAccessException;
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
        User user = (User) session.getAttribute("user");
        String requestURI = request.getRequestURI();
        if (!adminService.isAdmin(user.getId())) {
                session.invalidate();
                response.sendRedirect("/access-denied?redirectUrl=" + requestURI);
                return false;
            }
        return true;
    }
}

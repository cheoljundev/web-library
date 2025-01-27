package com.weblibrary.web.interceptor;

import com.weblibrary.web.SessionConst;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class SsrLoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(SessionConst.LOGIN_USER) == null) {
                response.sendRedirect("/access-denied?redirectUrl=" + requestURI);
                return false;
            }
        return true;
    }
}

package com.weblibrary.web.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblibrary.web.exception.UnauthorizedAccessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class RestLoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            throw new UnauthorizedAccessException("로그인해주세요.");
        }
        return true;
    }
}

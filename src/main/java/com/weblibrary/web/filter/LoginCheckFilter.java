package com.weblibrary.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    private static final String[] whiteList = { "/", "/login", "/join", "/signout", "/css/*", "/js/*", "/access-denied"};

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String requestURI = request.getRequestURI();

        HttpServletResponse response = (HttpServletResponse) servletResponse;


        try {
            if (isLoginCheckPath(requestURI)) {
                log.debug("requestURI={}", requestURI);
                HttpSession session = request.getSession(false);
                if (session == null) {

                    if (requestURI.startsWith("/books") || requestURI.startsWith("/users")) {
                        requestURI = "/";
                    }

                    response.sendRedirect("/access-denied?redirectUrl=" + requestURI);
                    return;
                }
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            throw e;
        }
    }

    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whiteList, requestURI);
    }

}

package com.weblibrary.controller.core;

import com.weblibrary.controller.Controller;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * 핸들러(컨트롤러)에 부착되는 어댑터 클래스입니다.
 * 지원확인, 추가적인 핸들링을 담당합니다.
 */
public interface HandlerAdapter {
    boolean supports(Controller handler);

    ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException;
}

package com.weblibrary.controller.core;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.*;

/**
 * view 파일의 실제 주소를 받아 RequestDispatcher 인스턴스로 뷰 파일을 보여주는 클래스
 */
@RequiredArgsConstructor
public class View {
    private final String viewPath;

    /**
     *
     * @param model : view 파일에서 사용할 model Map
     * @param request : request 객체
     * @param response : response 객체
     *
     * @throws ServletException
     * @throws IOException
     */
    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request,response);
    }

    /**
     * model을 받아 request의 attribute로 넣어주는 메서드
     *
     * @param model : view 파일에서 사용할 model Map
     * @param request : request 객체
     */
    private static void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
}

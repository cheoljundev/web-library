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
    /**
     * 핸들러가 이 어댑터 클래스를 지원하는지 확인
     *
     * @param handler : Controller
     * @return : boolean을 반환
     */
    boolean supports(Controller handler);

    /**
     * 컨트롤러를 핸들링하는 메서드. handler가 ModelView를 반환하든 안 하든 만들어서 반환하도록 유도
     *
     * @param request : request 객체
     * @param response : response 객체
     * @param handler : Controller 객체
     * @return : ModelView 객체를 만들어 반환
     * @throws ServletException
     * @throws IOException
     */
    ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException;
}

package com.weblibrary.core.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weblibrary.core.controller.Controller;
import com.weblibrary.core.controller.JsonResponseController;
import com.weblibrary.core.HandlerAdapter;
import com.weblibrary.core.view.ModelView;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * JsonContoller를 핸들링하는 어댑터
 */
public class JsonResponseControllerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Controller handler) {
        return (handler instanceof JsonResponseController);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        JsonResponseController controller = (JsonResponseController) handler;

        /* 객체 To Json을 위한 ObjectMapper */
        ObjectMapper mapper = new ObjectMapper();

        /* Jackson에 JavaTimeModule 등록 */
        mapper.registerModule(new JavaTimeModule());

        JsonResponse jsonResponse = controller.response(request);
        String result = mapper.writeValueAsString(jsonResponse);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(result);

        return null;
    }

}

package com.weblibrary.core.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.weblibrary.AppConfig;
import com.weblibrary.core.HandlerAdapter;
import com.weblibrary.core.controller.Controller;
import com.weblibrary.core.controller.JsonResponseController;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.core.view.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * JsonContoller를 핸들링하는 어댑터
 */
public class JsonResponseControllerAdapter implements HandlerAdapter {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final ObjectMapper mapper = appConfig.objectMapper();

    @Override
    public boolean supports(Controller handler) {
        return (handler instanceof JsonResponseController);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        JsonResponseController controller = (JsonResponseController) handler;

        JsonResponse jsonResponse = controller.response(request);

        if (jsonResponse != null) {
            String result = mapper.writeValueAsString(jsonResponse);

            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(result);
        }

        return null;
    }

}

package com.weblibrary.controller.core.adapter;

import com.weblibrary.controller.Controller;
import com.weblibrary.controller.UserController;
import com.weblibrary.controller.core.HandlerAdapter;
import com.weblibrary.controller.core.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserControllerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Controller handler) {
        return (handler instanceof UserController);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        UserController controller = (UserController) handler;

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        String viewName = "home/" + controller.process(paramMap, model);

        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
    }

    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}

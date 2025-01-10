package com.weblibrary.controller.core.adapter;

import com.weblibrary.controller.Controller;
import com.weblibrary.controller.UserRedirectController;
import com.weblibrary.controller.core.HandlerAdapter;
import com.weblibrary.controller.core.ModelView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * UserContoller를 핸들링하는 어댑터
 * Redirect를 처리한다.
 */
public class UserRedirectControllerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Controller handler) {
        return (handler instanceof UserRedirectController);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        UserRedirectController controller = (UserRedirectController) handler;

        Map<String, String> paramMap = createParamMap(request);

        String redirectURI = controller.process(request, paramMap);
        if (redirectURI != null) {
            response.sendRedirect(redirectURI);
        }
        return null;
    }

    /**
     * request에 들어온 paramNames로 param을 받아 Map에 저장
     *
     * @param request : request 객체
     * @return : paramMap
     */
    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}

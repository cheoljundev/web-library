package com.weblibrary.controller.core.adapter;

import com.weblibrary.controller.Controller;
import com.weblibrary.controller.UserForwardController;
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
 */
public class UserControllerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Controller handler) {
        return (handler instanceof UserForwardController);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        UserForwardController controller = (UserForwardController) handler;

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        /**
         * UserController는 view파일 경로에 home을 꼭 붙인다.
         * paramMap과 model을 process에서 가공
         */
        String processResult = controller.process(request, response, paramMap, model);

        /**
         * processResult이 null인 경우는 redirect된 경우이다. 이 경우 jsp를 그리지 않는다. (PRG패턴)
         * ModelView가 아닌 null을 반환한다.
         */
        if (processResult == null) {
            return null;
        }

        String viewName = "home/" + processResult;


        /* 논리적 이름(viewName)으로 ModelView 객체를 생성하고, model을 삽입 */
        ModelView mv = new ModelView(viewName);
        mv.setModel(model);

        return mv;
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

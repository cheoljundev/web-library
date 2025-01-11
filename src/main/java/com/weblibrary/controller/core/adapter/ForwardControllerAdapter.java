package com.weblibrary.controller.core.adapter;

import com.weblibrary.controller.Controller;
import com.weblibrary.controller.ForwardController;
import com.weblibrary.controller.core.HandlerAdapter;
import com.weblibrary.controller.core.ModelView;
import com.weblibrary.controller.usercontroller.JoinController;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ForwardContoller를 핸들링하는 어댑터
 */
public class ForwardControllerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Controller handler) {
        return (handler instanceof ForwardController);
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Controller handler) throws ServletException, IOException {
        ForwardController controller = (ForwardController) handler;

        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>();

        /**
         * UserController는 view파일 경로에 home을 꼭 붙인다.
         * paramMap과 model을 process에서 가공
         */
        String viewName = controller.process(request, response, paramMap, model);

        /**
         * processResult이 null인 경우는 컨트롤러가 다른 어댑터를 동시에 구현한 경우 선택되지 못한 경우이다.
         * @see JoinController
         * ModelView가 아닌 null을 반환한다.
         */
        if (viewName == null) {
            return null;
        }

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

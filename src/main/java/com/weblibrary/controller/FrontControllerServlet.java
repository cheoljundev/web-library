package com.weblibrary.controller;

import com.weblibrary.controller.core.HandlerAdapter;
import com.weblibrary.controller.core.ModelView;
import com.weblibrary.controller.core.MyView;
import com.weblibrary.controller.core.adapter.UserControllerAdapter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

@WebServlet(name = "frontControllerServlet", urlPatterns = "/site/*")
public class FrontControllerServlet extends HttpServlet {

    private final Map<String, Controller> handlerMappingMap = new HashMap<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerServlet() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/site", new UserIndexController());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new UserControllerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Controller handler = getHandler(request);

        System.out.println("handler = " + handler); // 핸들러 확인

        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        HandlerAdapter adapter = getHandlerAdapter(handler);
        ModelView mv = adapter.handle(request, response, handler);

        String viewName = mv.getViewName();
        MyView view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    private Controller getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI(); // URI 확인
        System.out.println("requestURI = " + requestURI);
        return handlerMappingMap.get(requestURI);
    }

    private HandlerAdapter getHandlerAdapter(Controller handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. hander=" + handler);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

}

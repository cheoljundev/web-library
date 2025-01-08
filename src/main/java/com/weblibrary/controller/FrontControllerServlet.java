package com.weblibrary.controller;

import com.weblibrary.controller.core.HandlerAdapter;
import com.weblibrary.controller.core.*;
import com.weblibrary.controller.core.adapter.*;
import com.weblibrary.controller.usercontroller.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.*;

/**
 * 모든 요청이 이 Servlet을 거쳐서 처리됩니다.

 * 현재는 /site/* 하위로 처리하고 있지만, 추후 /*로 처리되어야 합니다.
 * todo: /*로 처리
 */
@WebServlet(name = "frontControllerServlet", urlPatterns = "/site/*")
public class FrontControllerServlet extends HttpServlet {

    /* handler(Controller)가 매핑된 Map */
    private final Map<String, Controller> handlerMappingMap = new HashMap<>();

    /* handler를 다루는 Adapter 클래스가 들어 있는 List */
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    /**
     * 이 클래스가 생성되면서 handler(Controller)가 매핑된 Map, handlerAdapter가 들어있는 List가 초기화 됩니다.
     */
    public FrontControllerServlet() {
        initHandlerMappingMap();
        initHandlerAdapters();
    }

    /**
     * 컨트롤러 클래스를 URI에 맞게 등록해서 초기화 합니다.
     */
    private void initHandlerMappingMap() {
        handlerMappingMap.put("/site", new UserIndexController());
        handlerMappingMap.put("/site/join", new UserJoinController());
        handlerMappingMap.put("/site/login", new UserLoginController());
    }

    /**
     * 컨트롤러 클래스 다루는 어댑터 클래스들을 초기화 합니다.
     */
    private void initHandlerAdapters() {
        handlerAdapters.add(new UserControllerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* 핸들러 획득 */
        Controller handler = getHandler(request);

        /* 핸들러를 획득하지 못하면, 404 응답 */
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        /* 어댑터 획득 */
        HandlerAdapter adapter = getHandlerAdapter(handler);

        /* 어댑터에서 handle 메서드 호출 */
        ModelView mv = adapter.handle(request, response, handler);

        /* ModelView 객체가 null로 넘어온 경우(redirect된 경우) service 종료 */
        if (mv == null) {
            return;
        }

        /* ModelView 인스턴스에서 viewName(논리적 주소) 획득 */
        String viewName = mv.getViewName();

        /* 획득한 viewName으로 실제 뷰 위치 만들고 View 반환 */
        View view = viewResolver(viewName);

        view.render(mv.getModel(), request, response);
    }

    /**
     *
     * @param request : HttpServletRequest 인스턴스
     * @return : Controller 인스턴스를 핸들러로 반환
     */
    private Controller getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return handlerMappingMap.get(requestURI);
    }

    /**
     *
     * @param handler : Controller 인스턴스
     * @return : 핸들러에 맞는 Adapter 클래스를 반환
     */
    private HandlerAdapter getHandlerAdapter(Controller handler) {
        for (HandlerAdapter adapter : handlerAdapters) {
            /* 지원 여부 확인 */
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        /* 지원하는 어댑터가 없으면, null이 아닌 예외를 던짐 */
        throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. hander=" + handler);
    }

    /**
     * 논리적 주소를 받아 실제 뷰 파일이 있는 주소를 반환함
     *
     * @param viewName : 논리적 주소
     * @return : View를 만들어서 반환함
     */
    private static View viewResolver(String viewName) {
        return new View("/WEB-INF/views/" + viewName + ".jsp");
    }

}

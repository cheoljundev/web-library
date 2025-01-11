package com.weblibrary.core.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weblibrary.AppConfig;
import com.weblibrary.core.HandlerAdapter;
import com.weblibrary.core.adapter.ForwardControllerAdapter;
import com.weblibrary.core.adapter.JsonResponseControllerAdapter;
import com.weblibrary.core.adapter.RedirectControllerAdapter;
import com.weblibrary.core.controller.dto.response.ErrorResponse;
import com.weblibrary.core.controller.dto.response.JsonResponse;
import com.weblibrary.core.view.ModelView;
import com.weblibrary.core.view.View;
import com.weblibrary.domain.admin.controller.AdminPageController;
import com.weblibrary.domain.admin.controller.AdminUsersController;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.user.controller.IndexController;
import com.weblibrary.domain.user.controller.JoinController;
import com.weblibrary.domain.user.controller.LoginController;
import com.weblibrary.domain.user.controller.UserBookController;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 모든 요청이 이 Servlet을 거쳐서 처리됩니다.
 * <p>
 * 현재는 /site/* 하위로 처리하고 있지만, 추후 /*로 처리되어야 합니다.
 * todo: /*로 처리
 */
@WebServlet(name = "frontControllerServlet", urlPatterns = "/site/*")
public class FrontControllerServlet extends HttpServlet {

    /* handler(Controller)가 매핑된 Map */
    private final Map<String, Controller> handlerMappingMap = new HashMap<>();

    /* handler를 다루는 Adapter 클래스가 들어 있는 List */
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private final AppConfig appConfig = AppConfig.getInstance();
    private final ObjectMapper mapper = appConfig.objectMapper();

    /**
     * 이 클래스가 생성되면서 handler(Controller)가 매핑된 Map, handlerAdapter가 들어있는 List가 초기화 됩니다.
     */
    public FrontControllerServlet() {
        initHandlerMappingMap();
        initHandlerAdapters();

        //테스트를 위한 초기 세팅
        initUser();
        initBook();
    }

    /**
     * 컨트롤러 클래스를 URI에 맞게 등록해서 초기화 합니다.
     */
    private void initHandlerMappingMap() {
        handlerMappingMap.put("/site", new IndexController());
        handlerMappingMap.put("/site/join", new JoinController());
        handlerMappingMap.put("/site/login", new LoginController());
        handlerMappingMap.put("/site/books/*", new UserBookController());
        handlerMappingMap.put("/site/admin", new AdminPageController());
        handlerMappingMap.put("/site/users/*", new AdminUsersController());
    }

    /**
     * 컨트롤러 클래스 다루는 어댑터 클래스들을 초기화 합니다.
     */
    private void initHandlerAdapters() {
        handlerAdapters.add(new ForwardControllerAdapter());
        handlerAdapters.add(new RedirectControllerAdapter());
        handlerAdapters.add(new JsonResponseControllerAdapter());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        /* 핸들러 획득 */
        Controller handler = getHandler(request);

        /* 핸들러를 획득하지 못하면, 404 응답 */
        if (handler == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.setContentType("application/json");
            JsonResponse errorResponse = new ErrorResponse(
                    HttpServletResponse.SC_NOT_FOUND,
                    "Not Found"
            );
            String result = mapper.writeValueAsString(errorResponse);
            response.getWriter().write(result);
            return;
        }

        /* 어댑터 획득 */
        List<HandlerAdapter> handlerAdapters = getHandlerAdapter(handler);
        for (HandlerAdapter adapter : handlerAdapters) {
            /* 어댑터에서 handle 메서드 호출 */
            ModelView mv = adapter.handle(request, response, handler);
            /* ModelView 객체가 null로 넘어온 경우(redirect된 경우) service 종료 */
            if (mv == null) {
                continue;
            }

            /* ModelView 인스턴스에서 viewName(논리적 주소) 획득 */
            String viewName = mv.getViewName();

            /* 획득한 viewName으로 실제 뷰 위치 만들고 View 반환 */
            View view = viewResolver(viewName);

            view.render(mv.getModel(), request, response);
        }

    }

    /**
     * @param request : HttpServletRequest 인스턴스
     * @return : Controller 인스턴스를 핸들러로 반환
     */
    private Controller getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Controller handler = handlerMappingMap.get(requestURI);

        if (handler == null) {
            if (request.getRequestURI().startsWith("/site/books/")) {
                //user books 컨트롤러
                handler = handlerMappingMap.get("/site/books/*");
            } else if (request.getRequestURI().startsWith("/site/users/")) {
                //users 컨트롤러
                handler = handlerMappingMap.get("/site/users/*");
            }
        }

        return handler;
    }

    /**
     * @param handler : Controller 인스턴스
     * @return : 핸들러에 맞는 Adapter 클래스를 반환
     */
    private List<HandlerAdapter> getHandlerAdapter(Controller handler) {
        List<HandlerAdapter> list = new ArrayList<>();
        for (HandlerAdapter adapter : handlerAdapters) {
            /* 지원 여부 확인 */
            if (adapter.supports(handler)) {
                list.add(adapter);
            }
        }
        /* 지원하는 어댑터가 없으면, null이 아닌 예외를 던짐 */
        if (list.isEmpty()) {
            throw new IllegalArgumentException("handler adapter를 찾을 수 없습니다. hander=" + handler);
        }

        return list;

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

    /**
     * 메모리 리포지토리 환경에서 테스트를 위한 User init 메서드
     */
    private static void initUser() {
        AppConfig appConfig = AppConfig.getInstance();
        UserService userService = appConfig.userService();
        AdminService adminService = appConfig.adminService();
        UserRepository userRepository = appConfig.userRepository();

        userService.join("admin", "1111");
        userService.join("user", "1111");
        User admin = userRepository.findByUsername("admin");
        adminService.setUserAsAdmin(admin.getId());
    }

    /**
     * 메모리 리포지토리 환경에서 테스트를 위한 Book init 메서드
     */
    private static void initBook() {
        AppConfig appConfig = AppConfig.getInstance();
        AdminService adminService = appConfig.adminService();
        Book jpa = new Book(MemoryUserRepository.lastId++, "JPA", "12345");
        Book spring = new Book(MemoryUserRepository.lastId++, "SPRING", "45678");
        adminService.addBook(jpa);
        adminService.addBook(spring);
    }

}

package com.weblibrary.controller.usercontroller;

import com.weblibrary.AppConfig;
import com.weblibrary.controller.ForwardController;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Map;

/**
 * site home 접속시 처리할 컨트롤러
 */
public class IndexController implements ForwardController {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final BookService bookService = appConfig.bookService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            List<Book> books = bookService.findAll();
            request.setAttribute("books", books);
        }

        return "home/index";
    }
}

package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.UserForwardController;
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
public class UserIndexForwardController implements UserForwardController {

    private final BookService bookService = BookService.getInstance();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {
            List<Book> books = bookService.findAll();
            session.setAttribute("books", books);
        }

        return "index";
    }
}

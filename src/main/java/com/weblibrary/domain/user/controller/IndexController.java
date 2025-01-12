package com.weblibrary.domain.user.controller;

import com.weblibrary.AppConfig;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * site home 접속시 처리할 컨트롤러
 */
@Controller
public class IndexController {

    private final AppConfig appConfig = AppConfig.getInstance();
    private final BookService bookService = appConfig.bookService();

    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");

        if (user != null) {
            List<Book> books = bookService.findAll();
            model.addAttribute("books", books);
        }

        return "home/index";
    }
}

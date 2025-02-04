package com.weblibrary.web.controller;

import com.weblibrary.domain.book.dto.BookListItem;
import com.weblibrary.domain.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * site home 접속시 처리할 컨트롤러
 */
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BookService bookService;

    @GetMapping("/")
    public String index(Model model) {

        List<BookListItem> books = bookService.findAll();
        model.addAttribute("books", books);

        return "home/index";
    }
}

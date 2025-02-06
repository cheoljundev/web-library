package com.weblibrary.web.controller;

import com.weblibrary.domain.book.dto.BookListItem;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.web.util.PageBlock;
import com.weblibrary.web.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * site home 접속시 처리할 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class IndexController {

    private final BookService bookService;

    @GetMapping("/")
    public String index(Model model, Pageable pageable) {
        log.debug("pageable={}", pageable);
        Page<BookListItem> bookPage = bookService.findAll(pageable);

        int blockSize = 10; // 한 블록에 표시할 페이지 수
        PageBlock pageBlock = PaginationUtil.createPageBlock(bookPage, blockSize);

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageBlock", pageBlock);

        return "home/index";
    }
}

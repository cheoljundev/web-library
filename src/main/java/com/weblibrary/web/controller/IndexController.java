package com.weblibrary.web.controller;

import com.weblibrary.domain.book.dto.BookListItem;
import com.weblibrary.domain.book.service.BookService;
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
        int currentPage = bookPage.getNumber();
        int totalPages = bookPage.getTotalPages();

        // 현재 블록의 시작 페이지 (0부터 시작)
        int startPage = (currentPage / blockSize) * blockSize;
        // 현재 블록의 끝 페이지 (단, 전체 페이지 수를 넘지 않도록)
        int endPage = Math.min(startPage + blockSize, totalPages);

        // startPage부터 endPage까지 페이지 번호 목록 생성
        List<Integer> pageNumbers = new ArrayList<>();
        for (int i = startPage; i < endPage; i++) {
            pageNumbers.add(i);
        }

        model.addAttribute("bookPage", bookPage);
        model.addAttribute("pageNumbers", pageNumbers);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("blockSize", blockSize);

        return "home/index";
    }
}

package com.weblibrary.web.book.controller;

import com.weblibrary.domain.book.repository.BookSearchCond;
import com.weblibrary.domain.book.service.BookListItem;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.web.response.PageResponse;
import com.weblibrary.web.util.PageBlock;
import com.weblibrary.web.util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

/**
 * site home 접속시 처리할 컨트롤러
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping("/books")
    public ResponseEntity<PageResponse<BookListItem>> index(Pageable pageable, @ModelAttribute("cond") BookSearchCond cond) {
        log.debug("pageable={}", pageable);
        PageResponse<BookListItem> page = bookService.findAll(cond, pageable);
        log.debug("page={}", page);

        return ResponseEntity.ok(page);
    }
}

package com.weblibrary.web.book.controller;

import com.weblibrary.domain.book.repository.BookSearchCond;
import com.weblibrary.domain.book.service.BookInfo;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    public ResponseEntity<PageResponse<BookInfo>> getBooks(@ModelAttribute BookSearchCond cond, Pageable pageable) {
        log.debug("cond={}", cond);
        log.debug("pageable={}", pageable);
        PageResponse<BookInfo> page = bookService.findAll(cond, pageable);

        return ResponseEntity.ok(page);
    }

    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookInfo> getBook(@PathVariable("bookId") Long bookId) {
        BookInfo bookInfo = bookService.findBookInfoByBookId(bookId);
        return ResponseEntity.ok(bookInfo);
    }
}

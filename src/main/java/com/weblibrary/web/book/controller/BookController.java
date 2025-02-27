package com.weblibrary.web.book.controller;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.repository.BookSearchCond;
import com.weblibrary.domain.book.service.BookInfo;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.web.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * BookController는 도서 관련 요청을 처리하는 REST 컨트롤러입니다.
 */
@Tag(name = "Book API", description = "도서 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 도서 목록을 검색 조건과 페이지 정보에 따라 조회합니다.
     *
     * @param cond     검색 조건
     * @param pageable 페이지 정보
     * @return 도서 목록 페이지 응답
     */
    @Operation(summary = "도서 목록 페이지 반환", description = "도서 목록 페이지를 반환합니다.")
    @GetMapping("/books")
    public ResponseEntity<PageResponse<BookInfo>> getBooks(@ModelAttribute BookSearchCond cond, Pageable pageable) {
        log.debug("cond={}", cond);
        log.debug("pageable={}", pageable);
        PageResponse<BookInfo> page = bookService.findAll(cond, pageable);

        return ResponseEntity.ok(page);
    }

    /**
     * 도서 상세 정보를 조회합니다.
     *
     * @param bookId 도서 ID
     * @return 도서 상세 정보 응답
     */
    @Operation(summary = "도서 상세 정보 반환", description = "도서 상세 정보를 반환합니다.")
    @GetMapping("/books/{bookId}")
    public ResponseEntity<BookInfo> getBook(@PathVariable("bookId") Long bookId) {
        BookInfo bookInfo = bookService.findBookInfoByBookId(bookId)
                .orElseThrow(NotFoundBookException::new);
        return ResponseEntity.ok(bookInfo);
    }
}

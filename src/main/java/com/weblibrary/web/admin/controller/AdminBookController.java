package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.book.service.ModifyBookForm;
import com.weblibrary.domain.book.service.NewBookForm;
import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 관리자 도서 컨트롤러 클래스
 * 도서 추가, 삭제, 수정 기능을 제공합니다.
 */
@Tag(name = "Admin Book API", description = "관리자 도서 관리 API")
@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;
    private final ErrorResponseUtils errorResponseUtils;

    /**
     * 도서를 추가합니다.
     *
     * @param form          도서 정보 폼
     * @param bindingResult 폼 검증 결과
     * @param coverImage    도서 표지 이미지
     * @return JsonResponse 응답 객체
     */
    @Operation(summary = "도서 추가", description = "도서를 추가합니다.")
    @ResponseBody
    @PostMapping("/books/add")
    public ResponseEntity<JsonResponse> addBook(@RequestPart("bookData") @Validated NewBookForm form,
                                                BindingResult bindingResult,
                                                @RequestPart(name = "coverImage", required = false) MultipartFile coverImage) {

        form.setCoverImage(coverImage);

        log.debug("bindingResult.target={}", bindingResult.getTarget());
        log.debug("bindingResult.objectName={}", bindingResult.getObjectName());

        log.debug("form={}", form);
        log.debug("form.bookName={}", form.getBookName());
        log.debug("form.isbn={}", form.getIsbn());

        if (form.getCoverImage() == null || form.getCoverImage().isEmpty()) {
            bindingResult.rejectValue("coverImage", "NotBlank", "책 표지는 필수입니다.");
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return errorResponseUtils.handleValidationErrors(bindingResult);
        }

        bookService.save(form);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 등록되었습니다.")
                .build());
    }

    /**
     * 도서를 수정합니다.
     *
     * @param form          도서 정보 폼
     * @param bindingResult 폼 검증 결과
     * @param coverImage    도서 표지 이미지
     * @return JsonResponse 응답 객체
     */
    @Operation(summary = "도서 수정", description = "도서를 수정합니다.")
    @ResponseBody
    @PutMapping("/books/{bookId}")
    public ResponseEntity<JsonResponse> modifyBook(@RequestPart("bookData") @Validated ModifyBookForm form,
                                                   BindingResult bindingResult,
                                                   @RequestPart(name = "coverImage", required = false) MultipartFile coverImage) {

        form.setCoverImage(coverImage);

        log.debug("bindingResult.objectName={}", bindingResult.getObjectName());
        log.debug("bindingResult.target={}", bindingResult.getTarget());


        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return errorResponseUtils.handleValidationErrors(bindingResult);
        }

        bookService.modify(form);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 수정되었습니다.")
                .build());
    }

    /**
     * 도서를 삭제합니다.
     *
     * @param bookId 삭제할 도서의 ID
     * @return JsonResponse 응답 객체
     */
    @Operation(summary = "도서 삭제", description = "도서를 삭제합니다.")
    @ResponseBody
    @DeleteMapping("/books/{bookId}")
    public JsonResponse deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return JsonResponse.builder()
                .message("정상 삭제되었습니다.")
                .build();
    }
}

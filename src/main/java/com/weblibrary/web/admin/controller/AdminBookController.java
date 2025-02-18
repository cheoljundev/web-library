package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.book.service.ModifyBookForm;
import com.weblibrary.domain.book.service.NewBookForm;
import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;
    private final ErrorResponseUtils errorResponseUtils;

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

    @ResponseBody
    @DeleteMapping("/books/{bookId}")
    public JsonResponse deleteBook(@PathVariable("bookId") Long bookId) {
        bookService.deleteBook(bookId);
        return JsonResponse.builder()
                .message("정상 삭제되었습니다.")
                .build();
    }

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
}

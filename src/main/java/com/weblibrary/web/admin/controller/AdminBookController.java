package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.book.dto.BookListItem;
import com.weblibrary.domain.book.dto.ModifyBookForm;
import com.weblibrary.domain.book.dto.ModifyBookViewForm;
import com.weblibrary.domain.book.dto.NewBookForm;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.web.book.validation.BookAddValidator;
import com.weblibrary.web.book.validation.BookModifyValidator;
import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final BookService bookService;
    private final ErrorResponseUtils errorResponseUtils;

    @ModelAttribute("books")
    public List<BookListItem> books() {
        return bookService.findAll();
    }

    @ModelAttribute("addBook")
    public NewBookForm newBookForm() {
        return new NewBookForm();
    }

    @ModelAttribute("modifyBook")
    public ModifyBookViewForm modifyBookDto() {
        return new ModifyBookViewForm();
    }

    @GetMapping("/admin/book")
    public String adminBookPage() {
        return "admin/book";
    }

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
            bindingResult.rejectValue("coverImage", "NotBlank", null);
        }

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return errorResponseUtils.handleValidationErrors(bindingResult);
        }

        bookService.addBook(form);

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

        bookService.modifyBook(form);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 수정되었습니다.")
                .build());
    }
}

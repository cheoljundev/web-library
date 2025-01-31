package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.book.model.dto.BookListItem;
import com.weblibrary.domain.book.model.dto.ModifyBookForm;
import com.weblibrary.domain.book.model.dto.ModifyBookViewForm;
import com.weblibrary.domain.book.model.dto.NewBookForm;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final BookAddValidator bookAddValidator;
    private final BookModifyValidator bookModifyValidator;
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
    public ResponseEntity<JsonResponse> addBook(@RequestParam(required = false) String bookName,
                                                @RequestParam(required = false) String isbn,
                                                @RequestParam(required = false) MultipartFile coverImage) throws IOException {

        NewBookForm form = new NewBookForm(bookName, isbn, coverImage);
        BindingResult bindingResult = new BeanPropertyBindingResult(form, "newBookForm");
        log.debug("bindingResult.target={}", bindingResult.getTarget());
        log.debug("bindingResult.objectName={}", bindingResult.getObjectName());

        log.debug("form={}", form);
        log.debug("form.bookName={}", form.getBookName());
        log.debug("form.isbn={}", form.getIsbn());

        bookAddValidator.validate(form, bindingResult);

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
    public ResponseEntity<JsonResponse> modifyBook(@RequestParam(required = false) Long id,
                                                   @RequestParam(required = false) String bookName,
                                                   @RequestParam(required = false) String isbn,
                                                   @RequestParam(required = false) MultipartFile coverImage) throws IOException {

        ModifyBookForm form = new ModifyBookForm(id, bookName, isbn, coverImage);

        BindingResult bindingResult = new BeanPropertyBindingResult(form, "modifyBookForm");

        log.debug("bindingResult.objectName={}", bindingResult.getObjectName());
        log.debug("bindingResult.target={}", bindingResult.getTarget());

        bookModifyValidator.validate(form, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return errorResponseUtils.handleValidationErrors(bindingResult);
        }

        bookService.modifyBook(id, form);

        return ResponseEntity.ok().body(JsonResponse.builder()
                .message("정상 수정되었습니다.")
                .build());
    }
}

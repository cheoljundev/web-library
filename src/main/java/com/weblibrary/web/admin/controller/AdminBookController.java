package com.weblibrary.web.admin.controller;

import com.weblibrary.web.core.dto.response.ErrorResponse;
import com.weblibrary.web.core.dto.response.JsonResponse;
import com.weblibrary.web.core.validation.ValidationUtils;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.web.book.validation.BookAddValidator;
import com.weblibrary.web.book.validation.BookModifyValidator;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminService adminService;
    private final BookAddValidator bookAddValidator;
    private final BookModifyValidator bookModifyValidator;
    private final BookService bookService;
    private final AdminUtils adminUtils;
    private final ValidationUtils validationUtils;

    @ModelAttribute("books")
    public List<Book> books() {
        return bookService.findAll();
    }

    @ModelAttribute("addBook")
    public NewBookDto newBookModel() {
        return new NewBookDto();
    }

    @ModelAttribute("modifyBook")
    public ModifyBookDto modifyBookDto() {
        return new ModifyBookDto();
    }

    @GetMapping("/admin/book")
    public String adminBookPage(HttpSession session) {
        if (adminUtils.isDefault(session)) {
            return "redirect:/access-denied";
        }
        return "admin/book";
    }

    @ResponseBody
    @PostMapping("/books/add")
    public ResponseEntity<JsonResponse> addBook(HttpSession session, @Validated @RequestBody NewBookDto book, BindingResult bindingResult) {

        if (adminUtils.isDefault(session)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        log.debug("bindingResult.objectName={}", bindingResult.getObjectName());
        log.debug("bindingResult.target={}", bindingResult.getTarget());

        log.debug("book={}", book);
        log.debug("book.bookName={}", book.getBookName());
        log.debug("book.isbn={}", book.getIsbn());

        bookAddValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {

            log.debug("errors={}", bindingResult);
            return validationUtils.handleValidationErrors(bindingResult);
        }

        adminService.addBook(book);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 등록되었습니다.")
                .build(), HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<JsonResponse> deleteBook(HttpSession session, @PathVariable("bookId") Long bookId) {
        if (adminUtils.isDefault(session)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        Book removed = adminService.deleteBook(bookId);

        if (removed == null) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .message("삭제되지 않았습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 삭제되었습니다.")
                .build(), HttpStatus.OK);

    }

    @ResponseBody
    @PutMapping("/books/{bookId}")
    public ResponseEntity<JsonResponse> modifyBook(HttpSession session, @PathVariable("bookId") Long bookId, @Validated @RequestBody ModifyBookDto book, BindingResult bindingResult) {

        if (adminUtils.isDefault(session)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        log.debug("bindingResult.objectName={}", bindingResult.getObjectName());
        log.debug("bindingResult.target={}", bindingResult.getTarget());

        bookModifyValidator.validate(book, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return validationUtils.handleValidationErrors(bindingResult);
        }

        adminService.modifyBook(bookId, book);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 수정되었습니다.")
                .build(), HttpStatus.OK);

    }

}

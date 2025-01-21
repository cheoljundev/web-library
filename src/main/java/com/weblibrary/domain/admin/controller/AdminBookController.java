package com.weblibrary.domain.admin.controller;

import com.weblibrary.core.dto.response.ErrorResponse;
import com.weblibrary.core.dto.response.JsonResponse;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.book.validation.BookAddValidator;
import com.weblibrary.domain.book.validation.BookModifyValidator;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminService adminService;
    private final BookAddValidator bookAddValidator;
    private final BookModifyValidator bookModifyValidator;
    private final BookService bookService;

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
        User user = (User) session.getAttribute("user");

        if (user == null || !adminService.isAdmin(user.getId())) {
            return "redirect:/access-denied";
        }
        return "admin/book";
    }

    @ResponseBody
    @PostMapping("/books/add")
    public ResponseEntity<JsonResponse> addBook(HttpSession session, @Validated @RequestBody NewBookDto book, BindingResult bindingResult) {

        if (isDefault(session)) {
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
            return handleValidationErrors(bindingResult);
        }

        adminService.addBook(book);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 등록되었습니다.")
                .build(), HttpStatus.OK);
    }

    @ResponseBody
    @DeleteMapping("/books/{bookId}")
    public ResponseEntity<JsonResponse> deleteBook(HttpSession session, @PathVariable("bookId") Long bookId) {
        if (isDefault(session)) {
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
    public ResponseEntity<JsonResponse> modifyBook(HttpSession session, @PathVariable("bookId") Long bookId, @RequestBody ModifyBookDto modifyBookDto, BindingResult bindingResult) {

        if (isDefault(session)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        bookModifyValidator.validate(modifyBookDto, bindingResult);

        if (bindingResult.hasErrors()) {
            log.debug("errors={}", bindingResult);
            return handleValidationErrors(bindingResult);
        }

        adminService.modifyBook(bookId, modifyBookDto);

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 수정되었습니다.")
                .build(), HttpStatus.OK);

    }

    private boolean isDefault(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return !adminService.isAdmin(user.getId());
    }

    private static ResponseEntity<JsonResponse> handleValidationErrors(Errors bindingResult) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();

        for (ObjectError globalError : globalErrors) {
            errors.put(globalError.getCode(), globalError.getDefaultMessage());
        }
        for (FieldError error : fieldErrors) {
            errors.put(error.getField(), error.getDefaultMessage());
        }

        return new ResponseEntity<>(ErrorResponse.builder()
                .code("validation")
                .message("validation 실패")
                .errors(errors).build()
                , HttpStatus.BAD_REQUEST);
    }

}

package com.weblibrary.domain.admin.controller;

import com.weblibrary.core.dto.response.ErrorResponse;
import com.weblibrary.core.dto.response.JsonResponse;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.validation.BookAddValidator;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class AdminBookController {

    private final AdminService adminService;
    private final BookAddValidator bookAddValidator;

    @PostMapping("/add")
    public ResponseEntity<JsonResponse> addBook(HttpSession session, @Validated @RequestBody NewBookDto book) {

        if (isDefault(session)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(book, "book");

        log.debug("bindingResult.objectName={}", bindingResult.getObjectName()); // 이름을 book으로 바꾸고 싶다면?
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

    @DeleteMapping("/{bookId}")
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

    @PutMapping("/{bookId}")
    public ResponseEntity<JsonResponse> modifyBook(HttpSession session, HttpServletResponse response, @PathVariable("bookId") Long bookId, @RequestBody ModifyBookDto modifyBookDto) {

        if (isDefault(session)) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .code("roleError")
                    .message("권한이 없습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        Book oldBook = adminService.modifyBook(bookId, modifyBookDto);

        if (oldBook == null) {
            return new ResponseEntity<>(ErrorResponse.builder()
                    .message("수정되지 않았습니다.")
                    .build(), HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(JsonResponse.builder()
                .message("정상 수정되었습니다.")
                .build(), HttpStatus.OK);

    }

    private boolean isDefault(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return !adminService.isAdmin(user.getId());
    }

    private static ResponseEntity<JsonResponse> handleValidationErrors(BeanPropertyBindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();

        for (ObjectError globalError : globalErrors) {
            errors.put(globalError.getCode(), globalError.getDefaultMessage());
        }
        for (FieldError error : fieldErrors) {
            errors.put((StringUtils.hasText(error.getField()) ? error.getField() : error.getCode()), error.getDefaultMessage());
        }

        return new ResponseEntity<>(ErrorResponse.builder()
                .code("validation")
                .message("validation 실패")
                .errors(errors).build()
                , HttpStatus.BAD_REQUEST);
    }

}

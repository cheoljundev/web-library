package com.weblibrary.web.book.exception;

import com.weblibrary.domain.book.exception.DuplicateIsbnException;
import com.weblibrary.domain.book.exception.NotFoundBookCoverException;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.web.response.ErrorResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@RequiredArgsConstructor
public class BookExceptionAdvice {

    @ExceptionHandler(NotFoundBookException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundBookException() {
        Map<String, String> errors = new HashMap<>();
        errors.put("root", "찾을 수 없는 책입니다.");
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("not found book")
                .message("책을 찾을 수 없습니다.")
                .errors(errors).build());
    }

    @ExceptionHandler(NotFoundBookCoverException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundBookCoverException() {
        Map<String, String> errors = new HashMap<>();
        errors.put("root", "찾을 수 없는 책 표지입니다.");
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("not found book cover")
                .message("책표지를 찾을 수 없습니다.")
                .errors(errors).build());
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ErrorResponse> DuplicateIsbnException(DuplicateIsbnException e) {
        Map<String, String> errors = new HashMap<>();
        errors.put("isbn", e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("duplicated isbn")
                .message("책 수정 실패")
                .errors(errors).build());
    }
}

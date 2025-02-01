package com.weblibrary.web.book.exception;

import com.weblibrary.domain.book.exception.DuplicateIsbnException;
import com.weblibrary.domain.book.exception.NotFoundBookCoverException;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.ErrorResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class BookExceptionAdvice {

    private final ErrorResponseUtils errorResponseUtils;

    @ExceptionHandler(NotFoundBookException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundBookException() {
        return errorResponseUtils.handleNotFoundErrors("book");
    }

    @ExceptionHandler(NotFoundBookCoverException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundBookCoverException() {
        return errorResponseUtils.handleNotFoundErrors("cover");
    }

    @ExceptionHandler(DuplicateIsbnException.class)
    public ResponseEntity<ErrorResponse> DuplicateIsbnException(DuplicateIsbnException e) {
        return errorResponseUtils.handleExceptionError(e, "isbn");
    }
}

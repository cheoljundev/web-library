package com.weblibrary.web.book.exception;

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

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundBookError(NotFoundBookException e) {
        return errorResponseUtils.handleNotFoundErrors(e.getOBJECT_NAME());
    }
}

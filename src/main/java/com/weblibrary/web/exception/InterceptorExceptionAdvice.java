package com.weblibrary.web.exception;

import com.weblibrary.web.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InterceptorExceptionAdvice {

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler
    public ErrorResponse handleUnauthorizedAccessException(UnauthorizedAccessException e) {
        return ErrorResponse.builder()
                .message(e.getMessage())
                .build();
    }
}

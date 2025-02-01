package com.weblibrary.web.user.exception;

import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.ErrorResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class UserExceptionAdvice {

    private final ErrorResponseUtils errorResponseUtils;

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFoundBookError(NotFoundUserException e) {
        return errorResponseUtils.handleNotFoundErrors("user");
    }
}

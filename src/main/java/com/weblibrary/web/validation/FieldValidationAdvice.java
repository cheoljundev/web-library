package com.weblibrary.web.validation;

import com.weblibrary.web.response.ErrorResponseUtils;
import com.weblibrary.web.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class FieldValidationAdvice {

    private final ErrorResponseUtils errorResponseUtils;

    @ExceptionHandler
    public ResponseEntity<JsonResponse> handleError(MethodArgumentNotValidException e) {
        return errorResponseUtils.handleValidationErrors(e);
    }
}

package com.weblibrary.core.validation;

import com.weblibrary.core.dto.response.JsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@RequiredArgsConstructor
public class FieldValidationHandler {

    private final ValidationUtils validationUtils;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<JsonResponse> handleError(MethodArgumentNotValidException e) {
        return validationUtils.handleValidationErrors(e);
    }
}

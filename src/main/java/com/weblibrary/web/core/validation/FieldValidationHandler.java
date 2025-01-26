package com.weblibrary.web.core.validation;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.web.core.dto.response.ErrorResponse;
import com.weblibrary.web.core.dto.response.JsonResponse;
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

    @ExceptionHandler(NotFoundBookException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundBookError() {
        return validationUtils.handleNotFoundErrors("book");
    }
}

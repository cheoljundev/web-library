package com.weblibrary.web.response;

import com.weblibrary.domain.rental.exception.RentalException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class ErrorResponseUtils {

    /* 메시지 소스 빈 DI */
    private final MessageSource messageSource;

    public ResponseEntity<JsonResponse> handleValidationErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        List<ObjectError> globalErrors = bindingResult.getGlobalErrors();
        log.debug("Locale.getDefault()={}", Locale.getDefault());

        /* 글로벌 에러 담기 */
        for (ObjectError globalError : globalErrors) {
            errors.put(globalError.getCode(), globalError.getDefaultMessage());
        }

        for (FieldError fieldError : fieldErrors) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("validation")
                .message("validation 실패")
                .errors(errors).build());
    }

    public ResponseEntity<ErrorResponse> handleExceptionError(RuntimeException e, String objectName) {
        Map<String, String> errors = new HashMap<>();
        errors.put(objectName, e.getMessage());
        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("global")
                .errors(errors).build());
    }

    public ResponseEntity<ErrorResponse> handleNotFoundErrors(String object) {
        Map<String, String> errors = new HashMap<>();

        String errorMessage = messageSource.getMessage(
                "not.found." + object,
                null,
                Locale.getDefault());

        errors.put(object, errorMessage);

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("validation")
                .message("validation 실패")
                .errors(errors).build());
    }
}

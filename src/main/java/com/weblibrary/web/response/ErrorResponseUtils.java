package com.weblibrary.web.response;

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
            String errorMessage = messageSource.getMessage(
                    globalError.getCode(), //에러 코드 가지고 온다. 글로벌 에러는 별도의 code resolve 과정이 없다.
                    globalError.getArguments(), //argument 가지고 옴
                    Locale.getDefault()); //언어 정보는 기본 정보를 가져온다.
            errors.put(globalError.getCode(), errorMessage);
        }

        for (FieldError fieldError : fieldErrors) {
            String errorMessage = messageSource.getMessage(
                    fieldError.getCodes()[0], //에러 코드 가지고 온다. MessageCodesResolver에 의해서 가장 상세한 코드가 가장 앞으로 온다.
                    fieldError.getArguments(),
                    Locale.getDefault());
            errors.put(fieldError.getField(), errorMessage);
        }

        return ResponseEntity.badRequest().body(ErrorResponse.builder()
                .code("validation")
                .message("validation 실패")
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

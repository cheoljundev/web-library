package com.weblibrary.domain.file.exception;

import com.weblibrary.web.response.ErrorResponse;
import com.weblibrary.web.response.ErrorResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
public class FileExceptionAdvice {

    private final ErrorResponseUtils errorResponseUtils;

    @ExceptionHandler(NotFoundFileException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundFile() {
        return errorResponseUtils.handleNotFoundErrors("file");
    }
}

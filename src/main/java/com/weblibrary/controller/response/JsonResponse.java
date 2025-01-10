package com.weblibrary.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter @RequiredArgsConstructor
public abstract class JsonResponse {
    private final int status; // HTTP 상태 코드
    private final String message; // 에러 메시지
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp = LocalDateTime.now(); // 에러 발생 시간
}

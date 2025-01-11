package com.weblibrary.core.controller.dto.response;

public class ErrorResponse extends JsonResponse {
    public ErrorResponse(int status, String message) {
        super(status, message);
    }
}

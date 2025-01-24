package com.weblibrary.web.core.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@Data
@SuperBuilder
public class ErrorResponse extends JsonResponse {
    private final String code;
    private final Map<String, String> errors;

}

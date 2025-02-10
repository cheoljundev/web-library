package com.weblibrary.web.response;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.Map;

@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class ErrorResponse extends JsonResponse {
    private final String code;
    private final Map<String, String> errors;

}

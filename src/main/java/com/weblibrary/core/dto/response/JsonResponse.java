package com.weblibrary.core.dto.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JsonResponse {
    private String message;
}

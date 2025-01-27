package com.weblibrary.web.response;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public class JsonResponse {
    private String message;
}

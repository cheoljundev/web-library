package com.weblibrary.controller.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@RequiredArgsConstructor
@Getter
public class ModelView {
    private final String viewName;

    @Setter
    private Map<String, Object> model;
}

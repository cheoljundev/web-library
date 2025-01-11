package com.weblibrary.core.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

/**
 * model과 viewName이 들어 있는 클래스
 * viewName은 논리적 이름이다.
 */
@RequiredArgsConstructor
@Getter
public class ModelView {
    private final String viewName;

    @Setter
    private Map<String, Object> model;
}

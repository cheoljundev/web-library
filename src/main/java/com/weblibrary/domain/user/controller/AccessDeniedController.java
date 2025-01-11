package com.weblibrary.domain.user.controller;

import com.weblibrary.core.controller.ForwardController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class AccessDeniedController implements ForwardController {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) throws IOException {
        return "access-denied";
    }
}

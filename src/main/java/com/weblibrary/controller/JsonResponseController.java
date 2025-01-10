package com.weblibrary.controller;

import com.weblibrary.controller.dto.response.JsonResponse;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

public interface JsonResponseController extends Controller {
    JsonResponse response(HttpServletRequest request) throws IOException;
}
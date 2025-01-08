package com.weblibrary.controller.usercontroller;

import com.weblibrary.controller.UserController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * site home 접속시 처리할 컨트롤러
 */
public class UserIndexController implements UserController {
    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) {
        return "index";
    }
}

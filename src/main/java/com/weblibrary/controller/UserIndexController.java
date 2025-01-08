package com.weblibrary.controller;

import java.util.Map;

/**
 * site home 접속시 처리할 컨트롤러
 */
public class UserIndexController implements UserController {
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "index";
    }
}

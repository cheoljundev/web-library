package com.weblibrary.controller;

import java.util.Map;

public class UserIndexController implements UserController {
    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "index";
    }
}

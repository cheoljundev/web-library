package com.weblibrary.controller.admincontroller;

import com.weblibrary.AppConfig;
import com.weblibrary.controller.ForwardController;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class AdminPageController implements ForwardController {
    private final AppConfig appConfig = AppConfig.getInstance();
    private final AdminService adminService = appConfig.adminService();

    @Override
    public String process(HttpServletRequest request, HttpServletResponse response, Map<String, String> paramMap, Map<String, Object> model) {
        User user = (User) request.getSession().getAttribute("user");
        if (user != null) {
            if (adminService.isAdmin(user)) {
                return "admin/index";
            }
        }
        return "access-denied";
    }
}

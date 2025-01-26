package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AdminUtils {

    private final AdminService adminService;

    public boolean isDefault(User user) {
        log.debug("user={}", user);
        return user == null || !adminService.isAdmin(user.getId());
    }
}

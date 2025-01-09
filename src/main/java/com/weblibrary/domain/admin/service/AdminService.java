package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.*;

import static com.weblibrary.domain.user.model.Role.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminService {
    @Getter
    private static final AdminService instance = new AdminService();

    UserService userService = UserService.getInstance();

    public void setUserAsAdmin(String username) {
        User user = userService.findByUsername(username);
        user.setRole(Admin);
    }

    public void setUserAsDefault(String username) {
        User user = userService.findByUsername(username);
        user.setRole(Default);
    }

    public User removeUser(String username) {
        return userService.delete(username);
    }
}

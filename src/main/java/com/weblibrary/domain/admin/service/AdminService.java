package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.weblibrary.domain.user.model.Role.Admin;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AdminService {
    @Getter
    private static final AdminService instance = new AdminService();

    UserRepository userRepository = MemoryUserRepository.getInstance();

    UserService userService = UserService.getInstance();

    public void setUserAsAdmin(String username) {
        User user = userService.findByUsername(username);
        user.setRole(Admin);
    }

    public void setUserAsDefault(String username) {
        User user = userService.findByUsername(username);
        user.setRole(Role.User);
    }

    public User removeUser(String username) {
        return userService.delete(username);
    }
}

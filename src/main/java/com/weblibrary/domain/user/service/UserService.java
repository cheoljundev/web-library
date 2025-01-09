package com.weblibrary.domain.user.service;

import com.weblibrary.domain.admin.Repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.Repository.UserRoleRepository;
import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.weblibrary.domain.admin.model.RoleType.Default;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserService {
    @Getter
    private static final UserService instance = new UserService();
    private final UserRepository userRepository = MemoryUserRepository.getInstance();
    private final UserRoleRepository userRoleRepository = MemoryUserRoleRepository.getInstance();

    /**
     * 가입 처리 서비스 계층 메서드
     */
    public void join(String username, String password) {
        User user = new User(MemoryUserRepository.lastId++, username, password);
        Role role = new Role(MemoryUserRoleRepository.lastId, user.getId(), Default);
        userRoleRepository.save(role);
        userRepository.save(user);
    }

    /**
     * 로그인 처리를 담은 서비스 계층 메서드
     */
    public User login(String username, String password) {

        User user = userRepository.findByUsername(username);

        if (user.getPassword().equals(password)) {
            return user;
        }

        return null;

    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}

package com.weblibrary.domain.user.service;

import com.weblibrary.domain.admin.repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.repository.UserRoleRepository;
import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.weblibrary.domain.admin.model.RoleType.*;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * 가입 처리 서비스 계층 메서드
     */
    public void join(String username, String password) {
        User user = new User(MemoryUserRepository.lastId++, username, password);
        Role role = new Role(MemoryUserRoleRepository.lastId++, user.getId(), DEFAULT);
        userRoleRepository.save(role);
        userRepository.save(user);
    }

    /**
     * 로그인 처리를 담은 서비스 계층 메서드
     */
    public User login(String username, String password) {

        User foundUser = findByUsername(username);
        return authenticateUser(foundUser, password);
    }

    /**
     * 받은 User 객체와 password 파라미터가 일치하는지 확인하고 유저 반환
     *
     * @param user     : User 객체
     * @param password : password
     * @return : 일치하다면 유저 반환, 아니면 null 반환
     */
    private static User authenticateUser(User user, String password) {
        if (user != null) {
            if (user.getPassword().equals(password)) {
                return user;
            }
        }
        return null;
    }

    /**
     * username으로 유저를 찾음
     * @param username : String
     * @return : User
     */
    private User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

}

package com.weblibrary.domain.user.service;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.repository.UserRoleRepository;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.weblibrary.domain.admin.model.RoleType.DEFAULT;

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
     * Validator에서 호출한다.
     *
     * @param session : 세션
     * @param user    : Valitation에 성공한 유저
     */

    public void login(HttpSession session, User user) {
        session.setAttribute("user", user);
    }

    /**
     * username으로 유저를 찾음
     *
     * @param username : String
     * @return : User
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}

package com.weblibrary.domain.user.service;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.repository.UserRoleRepository;
import com.weblibrary.domain.user.dto.JoinUserDto;
import com.weblibrary.domain.user.dto.LoginUserForm;
import com.weblibrary.domain.user.exception.InvalidLoginException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.web.SessionConst;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.weblibrary.domain.admin.model.RoleType.DEFAULT;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;

    /**
     * 가입 처리 서비스 계층 메서드
     */
    public void join(JoinUserDto joinUserDto) {
        String username = joinUserDto.getUsername();
        String password = joinUserDto.getPassword();
        User user = new User(username, password);
        Role role = new Role(user.getUserId(), DEFAULT);
        userRoleRepository.save(role);
        userRepository.save(user);
    }

    /**
     * 로그인 처리를 담은 서비스 계층 메서드
     * Validator에서 호출한다.
     *
     * @param session      : 세션
     * @param form : Valitation에 성공한 유저Dto
     */

    public void login(HttpSession session, LoginUserForm form) {
        User loginUser = findByUsername(form.getUsername())
                .filter(user -> user.getPassword().equals(form.getPassword()))
                .orElseThrow(() -> new InvalidLoginException("로그인에 실패했습니다. 아이디 및 비밀번호를 확인하세요.", form));

        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
    }

    /**
     * username으로 유저를 찾음
     *
     * @param username : String
     * @return : User
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}

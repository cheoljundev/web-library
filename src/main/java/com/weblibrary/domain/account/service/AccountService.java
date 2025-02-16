package com.weblibrary.domain.account.service;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.repository.RoleRepository;
import com.weblibrary.domain.account.exception.InvalidJoinException;
import com.weblibrary.domain.account.exception.InvalidLoginException;
import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.SessionConst;
import com.weblibrary.web.account.controller.LoginUser;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.weblibrary.domain.user.model.RoleType.DEFAULT;

/**
 * AccountService 클래스는 사용자 계정 관련 비즈니스 로직을 처리합니다.
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 사용자 계정 관련 비즈니스 로직을 처리하는 서비스 클래스입니다.
     *
     * @param form : 사용자 회원가입 폼
     * @return : 사용자 엔티티
     */
    public User join(JoinUserForm form) {
        if (!isUniqueUsername(form.getUsername())) {
            throw new InvalidJoinException("이미 존재하는 유저이름입니다.", form);
        }
        String encodedPassword = passwordEncoder.encode(form.getPassword());
        User savedUser = userService.save(new User(form.getUsername(), encodedPassword));
        Role role = new Role(savedUser.getUserId(), DEFAULT);
        roleRepository.save(role);
        return savedUser;
    }

    /**
     * 사용자 로그인 처리
     *
     * @param session : 세션
     * @param form : 사용자 로그인 폼
     */
    @Transactional(readOnly = true)
    public void login(HttpSession session, LoginUserForm form) {
        LoginUser loginUser = userService.findByUsername(form.getUsername())
                .filter(user -> passwordEncoder.matches(form.getPassword(), user.getPassword()))
                .map(user -> new LoginUser(user.getUserId(), user.getUsername()))
                .orElseThrow(() -> new InvalidLoginException("로그인에 실패했습니다. 아이디 및 비밀번호를 확인하세요.", form));

        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
    }

    /**
     * 사용자 계정 삭제
     *
     * @param userId : 삭제할 사용자 ID
     */
    public void deleteUser(Long userId) {
        userService.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        userRepository.deleteById(userId);
    }

    /**
     * 사용자 이름의 고유성을 확인합니다.
     *
     * @param username : 확인할 사용자 이름
     * @return : 사용자 이름이 고유하면 true, 그렇지 않으면 false
     */
    private boolean isUniqueUsername(String username) {
        return userService.findByUsername(username).isEmpty();
    }
}

package com.weblibrary.domain.account.service;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.repository.UserRoleRepository;
import com.weblibrary.domain.account.exception.InvalidJoinException;
import com.weblibrary.domain.account.exception.InvalidLoginException;
import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import com.weblibrary.web.SessionConst;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.weblibrary.domain.user.model.RoleType.DEFAULT;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AccountService {

    private final UserRepository userRepository;
    private final UserService userService;
    private final UserRoleRepository userRoleRepository;

    /**
     * 가입 처리 서비스 계층 메서드
     */
    public User join(JoinUserForm form) {
        if (!isUniqueUsername(form.getUsername())) {
            throw new InvalidJoinException("이미 존재하는 유저이름입니다.", form);
        }
        User savedUser = userService.save(new User(form.getUsername(), form.getPassword()));
        Role role = new Role(savedUser.getUserId(), DEFAULT);
        userRoleRepository.save(role);
        return savedUser;
    }

    /**
     * 로그인 처리를 담은 서비스 계층 메서드
     * Validator에서 호출한다.
     *
     * @param session      : 세션
     * @param form : Valitation에 성공한 유저Dto
     */

    @Transactional(readOnly = true)
    public void login(HttpSession session, LoginUserForm form) {
        User loginUser = userService.findByUsername(form.getUsername())
                .filter(user -> user.getPassword().equals(form.getPassword()))
                .orElseThrow(() -> new InvalidLoginException("로그인에 실패했습니다. 아이디 및 비밀번호를 확인하세요.", form));

        session.setAttribute(SessionConst.LOGIN_USER, loginUser);
    }

    public void deleteUser(Long userId) {

        userService.findById(userId)
                .orElseThrow(NotFoundUserException::new);

        userRoleRepository.findRolesByUserId(userId).forEach(role -> {
            userRoleRepository.remove(role.getRoleId());
        });

        userRepository.remove(userId);
    }

    private boolean isUniqueUsername(String username) {
        return userService.findByUsername(username).isEmpty();
    }
}

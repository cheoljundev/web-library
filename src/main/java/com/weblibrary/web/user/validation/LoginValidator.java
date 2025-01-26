package com.weblibrary.web.user.validation;

import com.weblibrary.domain.user.dto.LoginUserDto;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator {

    private final UserService userService;
    public static final String LOGIN_ERROR = "loginGlobal";

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginUserDto user = (LoginUserDto) target;

        String password = user.getPassword();

        // 로그인 검증
        getFoundUser(user)
                .ifPresent(foundUser -> {
                    if (isInvalidPassword(foundUser, password)) {
                        errors.reject(LOGIN_ERROR, null, null);
                    }
                });
    }

    private Optional<User> getFoundUser(LoginUserDto user) {
        return userService.findByUsername(user.getUsername());
    }

    /**
     * 받은 User 객체와 password 파라미터가 일치하는지 확인하고 실패할 시 결과 반환
     *
     * @param user     : User 객체
     * @param password : password
     * @return : 일치하다면 true, 일치하지 않으면 false
     */
    private boolean isInvalidPassword(User user, String password) {

        if (user == null) {
            return true;
        }

        return !user.getPassword().equals(password);

    }
}

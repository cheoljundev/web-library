package com.weblibrary.domain.user.validation;

import com.weblibrary.domain.user.model.LoginUserDto;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginValidator implements Validator {

    private final UserService userService;
    private final HttpSession session;

    public static final String REQUIRED_FIELD = "required";
    public static final String LOGIN_ERROR = "loginGlobal";

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginUserDto user = (LoginUserDto) target;

        String username = user.getUsername();
        String password = user.getPassword();

        boolean isUsernameEmptyOrBlank = !StringUtils.hasText(username);
        boolean isPasswordEmptyOrBlank = !StringUtils.hasText(password);

        // 필수값 검증
        if (isUsernameEmptyOrBlank || isPasswordEmptyOrBlank) {
            if (isUsernameEmptyOrBlank) {
                errors.rejectValue("username", REQUIRED_FIELD);
            }
            if (isPasswordEmptyOrBlank) {
                errors.rejectValue("password", REQUIRED_FIELD);
            }
            return; // 이후 검증 불필요
        }

        // 로그인 검증
        User foundUser = getFoundUser(user);

        boolean authenticated = authenticateUser(foundUser, password);

        if (authenticated) {
            userService.login(session, foundUser);
        } else {
            errors.reject(LOGIN_ERROR, null, null);
        }

    }

    private User getFoundUser(LoginUserDto user) {
        return userService.findByUsername(user.getUsername());
    }

    /**
     * 받은 User 객체와 password 파라미터가 일치하는지 확인하고 유저 반환
     *
     * @param user     : User 객체
     * @param password : password
     * @return : 일치하다면 true, 일치하지 않으면 false
     */
    private boolean authenticateUser(User user, String password) {

        if (user == null) {
            return false;
        }

        return user.getPassword().equals(password);

    }
}

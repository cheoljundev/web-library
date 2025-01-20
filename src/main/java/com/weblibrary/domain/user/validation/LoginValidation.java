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
public class LoginValidation implements Validator {

    private final UserService userService;
    private final HttpSession session;

    @Override
    public boolean supports(Class<?> clazz) {
        return LoginUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LoginUserDto user = (LoginUserDto) target;

        if (!StringUtils.hasText(user.getUsername())) {
            errors.rejectValue("username", "required");
        } else if (!StringUtils.hasText(user.getPassword())) {
            errors.rejectValue("password", "required");
        } else {
            User loginUser = userService.login(user);

            log.debug("Login User: {}", loginUser);

            if (loginUser == null) {
                errors.reject("loginGlobal", null, null);
            } else {
                session.setAttribute("user", user);
            }

        }
    }
}

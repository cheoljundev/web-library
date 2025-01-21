package com.weblibrary.domain.user.validation;

import com.weblibrary.domain.user.model.JoinUserDto;
import com.weblibrary.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class JoinValidator implements Validator {

    private final UserService userService;

    public static final String REQUIRED_FIELD = "required";
    public static final String MIN_FIELD = "min";
    private static final String DUPLICATED_FIELD = "duplicated";
    private static final int MIN_USERNAME_LENGTH = 5;
    private static final int MIN_PASSWORD_LENGTH = 5;

    @Override
    public boolean supports(Class<?> clazz) {
        return JoinUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {


        JoinUserDto user = (JoinUserDto) target;

        // 필수값 검증
        String username = user.getUsername();
        String password = user.getPassword();

        boolean isUsernameEmptyOrBlank = !StringUtils.hasText(username);
        boolean isPasswordEmptyOrBlank = !StringUtils.hasText(password);

        boolean isUsernameTooShort = username.length() < MIN_USERNAME_LENGTH;
        boolean isPasswordTooShort = password.length() < MIN_PASSWORD_LENGTH;

        if (isUsernameEmptyOrBlank || isPasswordEmptyOrBlank || isUsernameTooShort || isPasswordTooShort) {
            if (isUsernameEmptyOrBlank) {
                errors.rejectValue("username", REQUIRED_FIELD);
            } else if (isUsernameTooShort) {
                log.debug("username length={}", username.length());
                errors.rejectValue("username", MIN_FIELD, new Object[]{MIN_USERNAME_LENGTH}, null);
            }

            if (isPasswordEmptyOrBlank) {
                errors.rejectValue("password", REQUIRED_FIELD);
            } else if (isPasswordTooShort) {
                errors.rejectValue("password", MIN_FIELD, new Object[]{MIN_PASSWORD_LENGTH}, null);
            }

            return; // 이후 검증 불필요
        }

        // 중복 계정 검증 검증
        if (isNotUniqueUsername(username)) {
            errors.rejectValue("username", DUPLICATED_FIELD);
        }


    }

    private boolean isNotUniqueUsername(String username) {
        return userService.findByUsername(username) != null;
    }
}

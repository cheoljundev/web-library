package com.weblibrary.web.user.validation;

import com.weblibrary.domain.user.dto.JoinUserDto;
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

    private static final String DUPLICATED_FIELD = "duplicated";

    @Override
    public boolean supports(Class<?> clazz) {
        return JoinUserDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {


        JoinUserDto user = (JoinUserDto) target;
        // 필수값 검증
        String username = user.getUsername();

        // 중복 계정 검증 검증
        if (isNotUniqueUsername(username)) {
            errors.rejectValue("username", DUPLICATED_FIELD);
        }


    }

    private boolean isNotUniqueUsername(String username) {
        return userService.findByUsername(username) != null;
    }
}

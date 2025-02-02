package com.weblibrary.domain.user.exception;

import com.weblibrary.domain.user.dto.LoginUserForm;
import lombok.Getter;

public class InvalidLoginException extends RuntimeException{
    @Getter
    private final LoginUserForm form;
    public InvalidLoginException(String message, LoginUserForm form) {
        super(message);
        this.form = form;
    }
}

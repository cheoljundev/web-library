package com.weblibrary.domain.account.exception;

import com.weblibrary.domain.account.service.LoginUserForm;
import lombok.Getter;

public class InvalidLoginException extends RuntimeException{
    @Getter
    private final LoginUserForm form;
    public InvalidLoginException(String message, LoginUserForm form) {
        super(message);
        this.form = form;
    }
}

package com.weblibrary.domain.account.exception;

import com.weblibrary.domain.account.service.JoinUserForm;
import lombok.Getter;

public class InvalidJoinException extends RuntimeException {
    @Getter
    private final JoinUserForm form;

    public InvalidJoinException(String message, JoinUserForm form) {
        super(message);
        this.form = form;
        form.setPassword("");
    }
}

package com.weblibrary.domain.account.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

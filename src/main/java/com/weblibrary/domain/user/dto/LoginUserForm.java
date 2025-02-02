package com.weblibrary.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserForm {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
}

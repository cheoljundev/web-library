package com.weblibrary.domain.account.service;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginUserForm {
    @NotBlank(message = "아이디를 입력해주세요.")
    private String username;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private String password;
}

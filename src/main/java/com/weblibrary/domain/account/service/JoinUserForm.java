package com.weblibrary.domain.account.service;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserForm {

    @Size(min = 5, message = "유저네임은 최소 5자 이상이어야 합니다.")
    @Pattern(regexp = "^[a-z][a-z0-9]*$", message = "아이디는 첫 글자가 소문자 영문이어야 하며, 소문자 영문과 숫자만 올 수 있습니다.")
    private String username;

    public void setUsername(String username) {
        this.username = (username != null) ? username.trim() : null;
    }

    @Size(min = 5, message = "비밀번호는 최소 5자 이상이어야 합니다.")
    @Pattern(regexp = "^[^\\s]+$", message = "비밀번호에는 공백을 포함할 수 없습니다.")
    private String password;
}

package com.weblibrary.domain.account.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserForm {

    @NotBlank
    @Size(min = 5)
    private String username;

    public void setUsername(String username) {
        this.username = (username != null) ? username.trim() : null;
    }

    @NotBlank
    @Size(min = 5)
    private String password;
}

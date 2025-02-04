package com.weblibrary.domain.account.dto;

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

    @NotBlank
    @Size(min = 5)
    private String password;
}

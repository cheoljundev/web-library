package com.weblibrary.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JoinUserDto {

    @NotBlank
    @Size(min = 5)
    private String username;

    @NotBlank
    @Size(min = 5)
    private String password;
}

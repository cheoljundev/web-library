package com.weblibrary.domain.book.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class ModifyBookDto {
    @NotNull
    private Long id;
    @NotBlank
    @Size(min = 5)
    private String bookName;
    @NotBlank
    @Size(min = 5)
    private String isbn;
}

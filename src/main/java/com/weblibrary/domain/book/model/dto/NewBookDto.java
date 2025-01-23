package com.weblibrary.domain.book.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewBookDto {

    @NotBlank
    @Size(min = 5)
    private String bookName;
    @NotBlank
    @Size(min = 5)
    private String isbn;
}

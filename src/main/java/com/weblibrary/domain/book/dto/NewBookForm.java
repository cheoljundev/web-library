package com.weblibrary.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewBookForm {

    @NotBlank
    private String bookName;

    @NotBlank
    private String isbn;

    private MultipartFile coverImage;
}

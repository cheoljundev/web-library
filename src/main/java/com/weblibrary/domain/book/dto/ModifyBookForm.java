package com.weblibrary.domain.book.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModifyBookForm {

    @NotNull
    private Long id;

    @NotBlank
    private String bookName;

    @NotBlank
    private String isbn;

    private MultipartFile coverImage;
}

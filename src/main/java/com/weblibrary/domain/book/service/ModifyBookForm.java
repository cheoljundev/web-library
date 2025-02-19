package com.weblibrary.domain.book.service;

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

    @NotNull(message = "정상적인 접근이 아닙니다.")
    private Long id;

    @NotBlank(message = "책 이름은 필수입니다.")
    private String bookName;

    @NotBlank(message = "저자는 필수입니다.")
    private String author;

    @NotBlank(message = "ISBN은 필수입니다.")
    private String isbn;

    @NotBlank(message = "설명은 필수입니다.")
    private String description;

    private MultipartFile coverImage;
}

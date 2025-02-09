package com.weblibrary.domain.book.service;

import jakarta.validation.constraints.NotBlank;
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
    private String author;

    @NotBlank
    private String isbn;

    private MultipartFile coverImage;
}

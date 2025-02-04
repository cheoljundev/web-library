package com.weblibrary.domain.book.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ModifyBookForm {
    private Long id;
    private String bookName;
    private String isbn;
    private MultipartFile coverImage;
}

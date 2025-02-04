package com.weblibrary.domain.book.dto;

import com.weblibrary.domain.file.model.UploadFile;
import lombok.Data;

@Data
public class ModifyBookViewForm {
    private Long id;
    private String bookName;
    private String isbn;
    private UploadFile coverImage;
}

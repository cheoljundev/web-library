package com.weblibrary.domain.book.model;

import com.weblibrary.domain.file.model.UploadFile;
import lombok.Data;

@Data
public class BookCover {
    private Long id;
    private final Long bookId;
    private final UploadFile image;
}

package com.weblibrary.domain.book.model;

import com.weblibrary.domain.file.model.UploadFile;
import lombok.Data;

@Data
public class BookCover {
    private Long bookCoverId;
    private final Book book;
    private final UploadFile image;
}

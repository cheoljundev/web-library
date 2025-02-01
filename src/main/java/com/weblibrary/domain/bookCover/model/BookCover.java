package com.weblibrary.domain.bookCover.model;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.file.model.UploadFile;
import lombok.Data;

@Data
public class BookCover {
    private Long bookCoverId;
    private final Book book;
    private final UploadFile image;
}

package com.weblibrary.domain.book.model;

import lombok.Data;

@Data
public class BookCover {
    private Long bookCoverId;
    private final Long bookId;
    private final Long uploadFileId;
}

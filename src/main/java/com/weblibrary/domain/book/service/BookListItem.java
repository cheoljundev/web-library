package com.weblibrary.domain.book.service;

import com.weblibrary.domain.file.model.UploadFile;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookListItem {
    private final Long id;
    private final String name;
    private final String isbn;
    private final UploadFile coverImage;
}

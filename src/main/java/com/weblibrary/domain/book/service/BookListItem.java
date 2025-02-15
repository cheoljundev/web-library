package com.weblibrary.domain.book.service;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookListItem {
    private final Long id;
    private final String bookName;
    private final String author;
    private final String isbn;
    private final String description;
    private final String coverImage;
}

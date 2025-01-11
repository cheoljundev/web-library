package com.weblibrary.domain.book.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class NewBookInfo {
    private final String bookName;
    private final String isbn;
}

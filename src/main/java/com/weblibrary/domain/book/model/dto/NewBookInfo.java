package com.weblibrary.domain.book.model.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter @Setter
public class NewBookInfo {
    private final String bookName;
    private final String isbn;
}

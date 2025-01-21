package com.weblibrary.domain.book.model.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookRentDto {
    private final Long userId;
    private final Long bookId;
}

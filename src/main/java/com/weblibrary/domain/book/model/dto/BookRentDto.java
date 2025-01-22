package com.weblibrary.domain.book.model.dto;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.user.model.User;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class BookRentDto {
    private final User user;
    private final Book book;
}

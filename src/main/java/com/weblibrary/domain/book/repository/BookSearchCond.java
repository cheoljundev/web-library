package com.weblibrary.domain.book.repository;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookSearchCond {
    private String bookName;
    private String isbn;
    private String author;
}

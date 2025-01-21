package com.weblibrary.domain.book.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewBookDto {
    private String bookName;
    private String isbn;
}

package com.weblibrary.domain.book.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ModifyBookDto {
    private Long id;
    private String bookName;
    private String isbn;
}

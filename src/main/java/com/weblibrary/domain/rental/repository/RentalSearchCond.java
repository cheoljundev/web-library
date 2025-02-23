package com.weblibrary.domain.rental.repository;

import lombok.Data;

@Data
public class RentalSearchCond {
    private String username;
    private String bookName;
    private String isbn;
    private String author;
    private Boolean returned;
}

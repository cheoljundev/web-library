package com.weblibrary.domain.book.model;

import com.weblibrary.domain.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Book {
    private final Long id;
    private String name;
    private String isbn;
    private boolean isRental;
    private User rentedBy;

    public Book(Long id, String name, String isbn) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
    }
}

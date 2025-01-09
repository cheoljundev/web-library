package com.weblibrary.domain.book.model;

import com.weblibrary.domain.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
public class Book {
    private Long id;
    private String name;
    private String isbn;
    private boolean isRental;
    private User rentedBy;

    public Book(Long id, String name, String isbn) {
        this.id = id;
        this.name = name;
        this.isbn = isbn;
    }

    public Book(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.isbn = book.getIsbn();
        this.isRental = book.isRental();
        this.rentedBy = book.getRentedBy();
    }

    public Book modify(Book newBook) {
        Book oldBook = new Book(this);

        this.id = newBook.getId();
        this.name = newBook.getName();
        this.isbn = newBook.getIsbn();
        this.isRental = newBook.isRental;
        this.rentedBy = newBook.rentedBy;

        return oldBook;
    }

    public boolean rent(User rentedBy) {
        if (!isRental) {
            this.rentedBy = rentedBy;
            isRental = true;
            return true;
        }

        return false;
    }

    public boolean unRent(User rentedBy) {
        if (isRental) {
            if (this.rentedBy == rentedBy) {
                isRental = false;
                this.rentedBy = null;
                return true;
            }
        }

        return false;
    }
}

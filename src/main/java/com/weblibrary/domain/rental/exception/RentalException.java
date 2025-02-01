package com.weblibrary.domain.rental.exception;

public class RentalException extends RuntimeException {
    public String objectName = "book";
    public RentalException(String message) {
        super(message);
    }
}

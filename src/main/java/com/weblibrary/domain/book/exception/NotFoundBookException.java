package com.weblibrary.domain.book.exception;

import lombok.Getter;

public class NotFoundBookException extends RuntimeException {

    @Getter private final String OBJECT_NAME = "book";

    public NotFoundBookException() {
    }
}

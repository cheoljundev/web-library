package com.weblibrary.domain.book.exception;

public class DuplicateIsbnException extends RuntimeException {
    public DuplicateIsbnException() {
        super("이미 등록된 isbn입니다.");
    }
}

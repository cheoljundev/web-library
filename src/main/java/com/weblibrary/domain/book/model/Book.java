package com.weblibrary.domain.book.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class Book {
    @Setter
    private Long id;
    private String name;
    private String isbn;
    private boolean isRented;

    public Book(String name, String isbn) {
        this.name = name;
        this.isbn = isbn;
    }

    /**
     * book 객체 자체를 받아서 새로운 book을 생성하는 생성자
     * 현재는 modify 메서드를 위해서 만들어둠
     *
     * @param book : 같은 값으로 생성할 book
     */
    public Book(Book book) {
        this.id = book.getId();
        this.name = book.getName();
        this.isbn = book.getIsbn();
        this.isRented = book.isRented();
    }

    /**
     * 새로운 book 정보를 받아 현재 객체를 수정하고, 기존 객체를 반환한다
     *
     * @return 기존 정보를담은 Book
     */
    public Book modify(String name, String isbn) {
        Book oldBook = new Book(this);
        this.name = name;
        this.isbn = isbn;

        return oldBook;
    }

    /**
     * 도서 대출 메서드
     *
     */
    public void rent() {
        isRented = true;
    }

    /**
     * 도서 반납 메서드
     *
     */
    public void unRent() {
        isRented = false;
    }
}
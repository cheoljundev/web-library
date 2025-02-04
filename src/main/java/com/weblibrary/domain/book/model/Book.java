package com.weblibrary.domain.book.model;

import lombok.*;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Book {
    @Setter
    private Long bookId;
    private String bookName;
    private String isbn;
    private boolean isRented;

    public Book(String bookName, String isbn) {
        this.bookName = bookName;
        this.isbn = isbn;
    }

    /**
     * book 객체 자체를 받아서 새로운 book을 생성하는 생성자
     * 현재는 modify 메서드를 위해서 만들어둠
     *
     * @param book : 같은 값으로 생성할 book
     */
    public Book(Book book) {
        this.bookId = book.getBookId();
        this.bookName = book.getBookName();
        this.isbn = book.getIsbn();
        this.isRented = book.isRented();
    }

    /**
     * 새로운 book 정보를 받아 현재 객체를 수정하고, 기존 객체를 반환한다
     *
     * @return 기존 정보를담은 Book
     */
    public Book modify(String bookName, String isbn) {
        Book oldBook = new Book(this);
        this.bookName = bookName;
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
package com.weblibrary.domain.book.model;

import com.weblibrary.domain.book.model.dto.ModifyBookDto;
import com.weblibrary.domain.user.model.User;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
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
        this.isRental = book.isRental();
        this.rentedBy = book.getRentedBy();
    }

    /**
     * 새로운 book 정보를 받아 현재 객체를 수정하고, 기존 객체를 반환한다
     *
     * @param newBookInfo : 변경할 book 정보
     * @return 기존 정보를담은 Book
     */
    public Book modify(ModifyBookDto newBookInfo) {
        Book oldBook = new Book(this);

        this.name = newBookInfo.getBookName();
        this.isbn = newBookInfo.getIsbn();

        return oldBook;
    }

    /**
     * 도서 대출 메서드
     * 대출상태를 변경하고, 대출한 사람을 기록한다.
     *
     * @param rentedBy : 대출할 유저
     */
    public void rent(User rentedBy) {
        isRental = true;
        this.rentedBy = rentedBy;
    }

    /**
     * 도서 반납 메서드
     * 대출 상태를 변경하고, 대출한 사람도 비운다.
     *
     * @param rentedBy : 대출했던 유저
     */
    public void unRent(User rentedBy) {
        isRental = false;
        this.rentedBy = null;
    }
}
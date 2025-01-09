package com.weblibrary.domain.book.model;

import com.weblibrary.domain.book.model.dto.ModifyBookInfo;
import com.weblibrary.domain.user.model.User;
import lombok.Getter;
import lombok.Setter;

@Getter
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
    public Book modify(ModifyBookInfo newBookInfo) {
        Book oldBook = new Book(this);

        this.name = newBookInfo.getName();
        this.isbn = newBookInfo.getIsbn();

        return oldBook;
    }

    /**
     * 도서 대출 메서드
     * 현재 도서가 대출 중이 아니면, 대출한다.
     *
     * @param rentedBy : 대출할 유저
     * @return : 성공 여부
     */
    public boolean rent(User rentedBy) {
        if (!isRental) {
            this.rentedBy = rentedBy;
            isRental = true;
            return true;
        }

        return false;
    }

    /**
     * 도서 반납 메서드
     * 현재 도서가 대출중이라면, 대출한 유저가 요청한 유저가 맞는지 확인하고, 반납한다.
     *
     * @param rentedBy : 대출했던 유저
     * @return : 성공 여부
     */
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

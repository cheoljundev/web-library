package com.weblibrary.domain.book.model;

import com.weblibrary.domain.book.model.dto.ModifyBookForm;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadRepository;
import com.weblibrary.domain.user.model.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;

@Getter
@ToString
@RequiredArgsConstructor
public class Book {
    @Setter
    private Long id;
    private String name;
    private String isbn;
    private boolean isRental;
    private User rentedBy;
    private UploadFile coverImage;

    public Book(String name, String isbn, UploadFile coverImage) {
        this.name = name;
        this.isbn = isbn;
        this.coverImage = coverImage;
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
        this.coverImage = book.getCoverImage();
    }

    /**
     * 새로운 book 정보를 받아 현재 객체를 수정하고, 기존 객체를 반환한다
     *
     * @param form : 변경할 book 정보
     * @return 기존 정보를담은 Book
     */
    public Book modify(Book newBook) {
        Book oldBook = new Book(this);

        this.name = newBook.getName();
        this.isbn = newBook.getIsbn();

        if (newBook.coverImage != null) {
            this.coverImage = newBook.coverImage;
        }

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
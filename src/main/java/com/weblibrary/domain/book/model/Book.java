package com.weblibrary.domain.book.model;

import com.weblibrary.domain.book.service.ModifyBookForm;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Book {
    @Setter
    @EqualsAndHashCode.Include
    @Id
    private Long bookId;
    private String bookName;
    private String author;
    private String isbn;
    private boolean rented;

    public Book(String bookName, String author, String isbn) {
        this.bookName = bookName;
        this.author = author;
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
        this.rented = book.isRented();
    }

    /**
     * 새로운 book 정보를 받아 현재 객체를 수정하고, 기존 객체를 반환한다
     *
     * @return 기존 정보를담은 Book
     */
    public Book modify(ModifyBookForm form) {
        Book oldBook = new Book(this);
        this.bookName = form.getBookName();
        this.author = form.getAuthor();
        this.isbn = form.getIsbn();

        return oldBook;
    }

    /**
     * 도서 대출 메서드
     *
     */
    public void rentBook() {
        rented = true;
    }

    /**
     * 도서 반납 메서드
     *
     */
    public void returnBook() {
        rented = false;
    }
}
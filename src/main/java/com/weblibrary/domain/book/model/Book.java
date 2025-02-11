package com.weblibrary.domain.book.model;

import com.weblibrary.domain.book.service.ModifyBookForm;
import com.weblibrary.web.response.JsonResponse;
import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "books")
public class Book {
    @Setter
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bookId;
    private String bookName;
    private String author;
    private String isbn;
    private String description;
    private boolean rented;

    public Book(String bookName, String author, String isbn, String description) {
        this.bookName = bookName;
        this.author = author;
        this.isbn = isbn;
        this.description = description;
    }

    /**
     * book 객체 자체를 받아서 새로운 book을 생성하는 생성자
     * 현재는 modify 메서드를 위해서 만들어둠
     *
     * @param book : 같은 값으로 생성할 book
     */
    private Book(Book book) {
        this(
                book.getBookId(),
                book.getBookName(),
                book.getAuthor(),
                book.getIsbn(),
                book.getDescription(),
                book.isRented()
        );
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
        this.description = form.getDescription();

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
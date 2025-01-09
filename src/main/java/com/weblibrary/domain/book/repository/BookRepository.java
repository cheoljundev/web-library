package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.user.model.User;

import java.util.List;

public interface BookRepository {
    /**
     * 책 저장
     * @param book : 저장할 책
     */
    void save(Book book);

    /**
     * id로 책을 찾음
     * @param id : 책 id
     * @return Book
     */
    Book findById(Long id);

    /**
     * 이름으로 책을 찾음
     * @param name : 책 이름
     * @return Book
     */
    Book findByName(String name);

    /**
     * 모든 책 리턴
     * @return Book List
     */
    List<Book> findAll();

    /**
     * 책 삭제
     * @param book : 삭제할 책
     * @return 삭제한 책 리턴
     */
    Book remove(Book book);

    /**
     * 모든 내용 삭제
     */
    void clearAll();

    Book findByIsbn(String isbn);
}

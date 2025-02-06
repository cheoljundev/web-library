package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BookRepository {
    /**
     * 책 저장
     * @param book : book 객체
     */
    Book save(Book book);

    /**
     * id로 책을 찾음
     * @param id : 책 id
     * @return Book
     */
    Optional<Book> findById(Long id);

    /**
     * 이름으로 책을 찾음
     * @param name : 책 이름
     * @return Book
     */
    Optional<Book> findByName(String name);

    Optional<Book> findByIsbn(String isbn);

    /**
     * 페이징 처리된 책 리스트 리턴
     * @param pageable : 페이징 정보
     * @return
     */
    Page<Book> findAll(Pageable pageable);

    /**
     * 책 삭제
     * @param bookId : 삭제할 책 id
     * @return 삭제한 책 리턴
     */
    Optional<Book> remove(Long bookId);

    Book update(Book book);

}

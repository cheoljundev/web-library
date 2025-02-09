package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface BookMapper {
    /**
     * 책 저장
     * @param book : book 객체
     */
    void save(Book book);

    /**
     * id로 책을 찾음
     *
     * @param bookId : 책 id
     * @return Book
     */
    Optional<Book> findById(Long bookId);

    /**
     * 이름으로 책을 찾음
     *
     * @param bookName : 책 이름
     * @return Book
     */
    Optional<Book> findByName(String bookName);

    Optional<Book> findByIsbn(String isbn);

    /**
     * 페이징 처리된 책 리스트 리턴
     *
     * @param limit
     * @param offset
     * @return
     */
    List<Book> findAll(@Param("limit") Number limit, @Param("offset") Number offset);

    int countAll();

    /**
     * 책 삭제
     *
     * @param bookId : 삭제할 책 id
     */
    void remove(Long bookId);

    void update(Book book);

}

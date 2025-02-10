package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface BookCoverMapper {
    void save(BookCover cover);
    Optional<BookCover> findById(Long coverId);
    Optional<BookCover> findByBookId(Long bookId);
    void update(BookCover cover);
    void remove(Long coverId);
}

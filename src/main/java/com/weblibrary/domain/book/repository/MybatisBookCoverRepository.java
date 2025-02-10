package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisBookCoverRepository implements BookCoverRepository {

    private final BookCoverMapper mapper;

    @Override
    public BookCover save(BookCover cover) {
        mapper.save(cover);
        return cover;
    }

    @Override
    public void remove(Long coverId) {
        mapper.remove(coverId);
    }

    @Override
    public Optional<BookCover> findById(Long coverId) {
        return mapper.findById(coverId);
    }

    @Override
    public Optional<BookCover> findByBookId(Long bookId) {
        return mapper.findByBookId(bookId);
    }

    @Override
    public void update(BookCover cover) {
        mapper.update(cover);
    }
}

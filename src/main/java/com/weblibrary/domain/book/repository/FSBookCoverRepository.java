package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FSBookCoverRepository implements BookCoverRepository {

    private final static Map<Long, BookCover> store = new HashMap<>();
    private static Long lastId = 0L;

    @Override
    public void save(BookCover cover) {
        cover.setBookCoverId(incrementLastId());
        store.put(cover.getBookCoverId(), cover);
    }

    @Override
    public void remove(Long coverId) {
        store.remove(coverId);
    }

    private Long incrementLastId() {
        return ++lastId;
    }

    @Override
    public BookCover findById(Long coverId) {
        return store.get(coverId);
    }

    @Override
    public BookCover findByBookId(Long bookId) {

        return findAll().stream()
                .filter(bookCover -> bookCover.getBook().getBookId().equals(bookId))
                .findFirst()
                .orElse(null);

    }

    @Override
    public List<BookCover> findAll() {
        return new ArrayList<>(store.values());
    }
}

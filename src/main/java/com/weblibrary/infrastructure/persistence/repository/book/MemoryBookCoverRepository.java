package com.weblibrary.infrastructure.persistence.repository.book;

import com.weblibrary.domain.book.model.BookCover;
import com.weblibrary.domain.book.repository.BookCoverRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
public class MemoryBookCoverRepository implements BookCoverRepository {

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
    public Optional<BookCover> findById(Long coverId) {
        return Optional.ofNullable(store.get(coverId));
    }

    @Override
    public Optional<BookCover> findByBookId(Long bookId) {

        return findAll().stream()
                .filter(bookCover -> bookCover.getBookId().equals(bookId))
                .findFirst();

    }

    @Override
    public List<BookCover> findAll() {
        return new ArrayList<>(store.values());
    }
}

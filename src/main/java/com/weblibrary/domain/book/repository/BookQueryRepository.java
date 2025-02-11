package com.weblibrary.domain.book.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weblibrary.domain.book.model.Book;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.weblibrary.domain.book.model.QBook.book;

@Repository
@Slf4j
public class BookQueryRepository {
    private final JPAQueryFactory query;

    public BookQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Book> findAll(BookSearchCond cond, Number limit, Number offset) {
        log.debug("cond={}", cond);

        List<Book> fetch = query.selectFrom(book)
                .where(
                        likeBookName(cond.getBookName()),
                        likeAuthor(cond.getAuthor()),
                        eqIsbn(cond.getIsbn())
                )
                .orderBy(book.bookId.desc())
                .limit(limit.longValue())
                .offset(offset.longValue())
                .fetch();

        log.debug("fetch={}", fetch);
        return fetch;
    }

    public long count(BookSearchCond cond) {
        return query.selectFrom(book)
                .where(
                        likeBookName(cond.getBookName()),
                        likeAuthor(cond.getAuthor()),
                        eqIsbn(cond.getIsbn())
                )
                .fetch()
                .size();
    }

    private BooleanExpression eqIsbn(String isbn) {
        return StringUtils.hasText(isbn) ? book.isbn.eq(isbn) : null;
    }

    private BooleanExpression likeAuthor(String author) {
        return StringUtils.hasText(author) ? book.author.contains(author) : null;
    }

    private BooleanExpression likeBookName(String bookName) {
        return StringUtils.hasText(bookName) ? book.bookName.contains(bookName) : null;
    }
}

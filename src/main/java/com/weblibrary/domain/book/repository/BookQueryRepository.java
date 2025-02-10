package com.weblibrary.domain.book.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weblibrary.domain.book.model.Book;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.weblibrary.domain.book.model.QBook.book;

@Repository
public class BookQueryRepository {
    private final JPAQueryFactory query;

    public BookQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public List<Book> findAll(BookSearchCond cond, Number limit, Number offset) {
        return query.selectFrom(book)
                .where(
                        likeBookName(cond.getBookName()),
                        likeAuthor(cond.getAuthor()),
                        eqIsbn(cond.getIsbn())
                )
                .limit(limit.longValue())
                .offset(offset.longValue())
                .fetch();
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
        return isbn != null ? book.isbn.eq(isbn) : null;
    }

    private BooleanExpression likeAuthor(String author) {
        return author != null ? book.author.contains(author) : null;
    }

    private BooleanExpression likeBookName(String bookName) {
        return bookName != null ? book.bookName.contains(bookName) : null;
    }
}

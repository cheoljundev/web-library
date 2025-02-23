package com.weblibrary.domain.rental.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weblibrary.domain.rental.model.QRental;
import com.weblibrary.domain.rental.model.Rental;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static com.querydsl.jpa.JPAExpressions.select;
import static com.weblibrary.domain.book.model.QBook.book;
import static com.weblibrary.domain.rental.model.QRental.rental;
import static com.weblibrary.domain.user.model.QUser.user;

@Repository
public class RentalQueryRepository {

    private final JPAQueryFactory query;

    public RentalQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Optional<Rental> findActiveRentalByBookId(Long bookId) {
        Rental rental = query.selectFrom(QRental.rental)
                .where(QRental.rental.returnedAt.isNull(), QRental.rental.bookId.eq(bookId))
                .fetchFirst();

        return Optional.ofNullable(rental);
    }

    public List<Rental> findAll(RentalSearchCond cond, int pageSize, long offset) {

        return query.selectFrom(rental)
                .where(
                        likeUserName(cond.getUsername()),
                        likeBookName(cond.getBookName()),
                        eqIsbn(cond.getIsbn()),
                        likeAuthor(cond.getAuthor()),
                        eqReturned(cond.getReturned())
                )
                .orderBy(rental.rentedAt.desc())
                .offset(offset)
                .limit(pageSize)
                .fetch();
    }

    public long count(RentalSearchCond cond) {
        return query.selectFrom(rental)
                .where(
                        likeUserName(cond.getUsername()),
                        likeBookName(cond.getBookName()),
                        eqIsbn(cond.getIsbn()),
                        likeAuthor(cond.getAuthor()),
                        eqReturned(cond.getReturned())
                )
                .fetch().size();
    }

    private BooleanExpression likeUserName(String username) {
        if (!StringUtils.hasText(username)) {
            return null;
        }
        return rental.userId.eq(
                select(user.userId)
                        .from(user)
                        .where(user.username.contains(username))
        );
    }

    private BooleanExpression likeBookName(String bookName) {
        if (!StringUtils.hasText(bookName)) {
            return null;
        }

        return rental.bookId.in(
                select(book.bookId)
                        .from(book)
                        .where(book.bookName.contains(bookName))
        );
    }

    private BooleanExpression eqIsbn(String isbn) {
        if (!StringUtils.hasText(isbn)) {
            return null;
        }
        return rental.bookId.eq(
                select(book.bookId)
                        .from(book)
                        .where(book.isbn.eq(isbn))
        );
    }

    private BooleanExpression likeAuthor(String author) {
        if (!StringUtils.hasText(author)) {
            return null;
        }
        return rental.bookId.in(
                select(book.bookId)
                        .from(book)
                        .where(book.author.contains(author))
        );
    }

    private BooleanExpression eqReturned(Boolean returned) {
        if (returned == null) {
            return null;
        }
        return returned ? rental.returnedAt.isNotNull() : rental.returnedAt.isNull();
    }
}

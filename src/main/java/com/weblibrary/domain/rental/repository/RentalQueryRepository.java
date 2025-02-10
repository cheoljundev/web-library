package com.weblibrary.domain.rental.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.weblibrary.domain.rental.model.QRental;
import com.weblibrary.domain.rental.model.Rental;
import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.weblibrary.domain.rental.model.QRental.*;

@Repository
public class RentalQueryRepository {

    private final JPAQueryFactory query;

    public RentalQueryRepository(EntityManager em) {
        this.query = new JPAQueryFactory(em);
    }

    public Optional<Rental> findActiveRentalByBookId(Long bookId) {
        Rental rental = query.selectFrom(QRental.rental)
                .where(QRental.rental.returnedAt.isNull())
                .fetchFirst();

        return Optional.ofNullable(rental);
    }
}

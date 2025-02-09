package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.model.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisRentalRepository implements RentalRepository {

    private final RentalMapper mapper;

    @Override
    public Rental save(Rental rental) {
        mapper.save(rental);
        return rental;
    }

    @Override
    public Optional<Rental> findActiveRentalByBookId(Long bookId) {
        return mapper.findActiveRentalByBookId(bookId);
    }

    @Override
    public List<Rental> findRentalsByUserId(Long userId) {
        return mapper.findRentalsByUserId(userId);
    }

    @Override
    public Optional<Rental> findById(Long rental_id) {
        return mapper.findById(rental_id);
    }

    @Override
    public void update(Rental rental) {
        mapper.update(rental);
    }

    @Override
    public void remove(Long rental_id) {
        mapper.remove(rental_id);
    }
}

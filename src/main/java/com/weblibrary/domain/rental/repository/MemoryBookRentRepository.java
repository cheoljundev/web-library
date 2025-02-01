package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.model.Rental;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MemoryBookRentRepository implements BookRentalRepository{
    private final Map<Long, Rental> rentals = new HashMap<>();
    private final AtomicLong rentalIdGenerator = new AtomicLong(1);

    @Override
    public Rental save(Rental rental) {
        long newId = rentalIdGenerator.getAndIncrement();
        rental.setRentalId(newId);
        rentals.put(rental.getRentalId(), rental);
        return rental;
    }

    @Override
    public Optional<Rental> findActiveRentalByBookId(Long bookId) {
        return rentals.values().stream()
                .filter(rental -> rental.getBookId().equals(bookId) && !rental.isReturned())
                .findFirst();
    }

    @Override
    public List<Rental> findRentalsByUserId(Long userId) {
        return rentals.values().stream()
                .filter(rental -> rental.getUserId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Rental> findById(Long id) {
        return Optional.ofNullable(rentals.get(id));
    }

    @Override
    public void delete(Rental rental) {
        rentals.remove(rental.getRentalId());
    }
}

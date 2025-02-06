package com.weblibrary.infra.persistence.repository.rental;

import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.rental.repository.BookRentalRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class MemoryBookRentRepository implements BookRentalRepository {
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
    public Optional<Rental> findById(Long rental_id) {
        return Optional.ofNullable(rentals.get(rental_id));
    }

    @Override
    public void delete(Long rental_id) {
        rentals.remove(rental_id);
    }
}

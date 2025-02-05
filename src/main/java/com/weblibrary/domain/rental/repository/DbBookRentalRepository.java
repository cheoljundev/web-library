package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.model.Rental;

public interface DbBookRentalRepository {
    Rental update(Rental rental);
}

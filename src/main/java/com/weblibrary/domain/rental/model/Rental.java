package com.weblibrary.domain.rental.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@ToString
public class Rental {
    @Setter
    private Long rentalId; // 고유 대출 ID
    private final Long bookId;
    private final Long userId;
    private final LocalDateTime rentedAt;
    private LocalDateTime returnedAt;

    public Rental(Long bookId, Long userId) {
        this.bookId = bookId;
        this.userId = userId;
        this.rentedAt = LocalDateTime.now();
    }

    public void returnBook() {
        this.returnedAt = LocalDateTime.now();
    }

    public boolean isReturned() {
        return returnedAt != null;
    }
}

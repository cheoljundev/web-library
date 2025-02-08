package com.weblibrary.domain.rental.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
@ToString
@AllArgsConstructor
public class Rental {
    @Setter
    private Long rentalId; // 고유 대출 ID
    private final Long bookId;
    private final Long userId;
    private final LocalDateTime rentedAt;
    private LocalDateTime returnedAt;

    public Rental(Long userId, Long bookId) {
        this.userId = userId;
        this.bookId = bookId;
        this.rentedAt = LocalDateTime.now();
    }

    public void returnBook() {
        this.returnedAt = LocalDateTime.now();
    }

    public boolean isReturned() {
        return returnedAt != null;
    }
}

package com.weblibrary.domain.rental.service;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weblibrary.domain.book.service.BookInfo;
import com.weblibrary.domain.user.service.UserInfo;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RentalInfo {
    private Long rentalId;
    private UserInfo user;
    private BookInfo book;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime rentedAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime returnedAt;
    private Boolean returned;

    public RentalInfo(Long rentalId, UserInfo user, BookInfo book, LocalDateTime rentedAt, LocalDateTime returnedAt) {
        this.rentalId = rentalId;
        this.user = user;
        this.book = book;
        this.rentedAt = rentedAt;
        this.returnedAt = returnedAt;
        this.returned = returnedAt != null;
    }
}

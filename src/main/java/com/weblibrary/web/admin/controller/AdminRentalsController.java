package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.rental.repository.RentalSearchCond;
import com.weblibrary.domain.rental.service.RentalInfo;
import com.weblibrary.domain.rental.service.RentalService;
import com.weblibrary.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminRentalsController {

    private final RentalService rentalService;

    @GetMapping("/books/rentals")
    public ResponseEntity<PageResponse<RentalInfo>> rentals(@ModelAttribute("cond") RentalSearchCond cond, Pageable pageable) {
        PageResponse<RentalInfo> page = rentalService.findAll(cond, pageable);
        log.debug("cond={}", cond);
        log.debug("page={}", page);
        return ResponseEntity.ok(page);
    }
}

package com.weblibrary.web.admin.controller;

import com.weblibrary.domain.rental.repository.RentalSearchCond;
import com.weblibrary.domain.rental.service.RentalInfo;
import com.weblibrary.domain.rental.service.RentalService;
import com.weblibrary.web.response.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin Rental API", description = "관리자 대여 관리 API")
@Slf4j
@RestController
@RequiredArgsConstructor
public class AdminRentalsController {

    private final RentalService rentalService;

    @Operation(summary = "대여 목록", description = "대여 목록을 반환합니다.")
    @GetMapping("/books/rentals")
    public ResponseEntity<PageResponse<RentalInfo>> rentals(@ModelAttribute("cond") RentalSearchCond cond, Pageable pageable) {
        PageResponse<RentalInfo> page = rentalService.findAll(cond, pageable);
        log.debug("cond={}", cond);
        log.debug("page={}", page);
        return ResponseEntity.ok(page);
    }
}

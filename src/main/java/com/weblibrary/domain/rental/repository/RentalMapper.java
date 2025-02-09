package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.model.Rental;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper
public interface RentalMapper {

    /**
     * 대출 정보 저장 (새로운 Rental 객체 저장)
     *
     * @param rental 대출 정보
     * @return 저장된 Rental 객체 (ID 포함)
     */
    void save(Rental rental);

    /**
     * 특정 책을 대출한 사용자 ID 조회
     *
     * @param bookId 대출된 책 ID
     * @return Optional<Rental>
     */
    Optional<Rental> findActiveRentalByBookId(Long bookId);

    /**
     * 특정 사용자 ID로 대출 기록 조회
     *
     * @param userId 사용자 ID
     * @return 대출 목록
     */
    List<Rental> findRentalsByUserId(Long userId);

    /**
     * 특정 rentalId로 대출 기록 조회
     *
     * @param rental_id 대출 ID
     * @return Optional<Rental>
     */
    Optional<Rental> findById(Long rental_id);

    void update(Rental rental);

    /**
     * 대출 기록 삭제 (반납 처리)
     *
     * @param rental_id 대출 정보
     */
    void remove(Long rental_id);
}
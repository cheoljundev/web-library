package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.model.Rental;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRentalRepository implements BookRentalRepository {

    private final JdbcTemplate template;

    @Override
    public Rental save(Rental rental) {
        String sql = "insert into rentals(book_id, user_id, rented_at) values(?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, rental.getBookId());
            pstmt.setLong(2, rental.getUserId());
            pstmt.setTimestamp(3, Timestamp.valueOf(rental.getRentedAt()));
            return pstmt;
        }, keyHolder);
        rental.setRentalId(keyHolder.getKey().longValue());
        return rental;
    }

    @Override
    public Optional<Rental> findActiveRentalByBookId(Long bookId) {
        String sql = "select * from rentals where book_id = ? and returned_at is null";
        try {
            Rental rental = template.queryForObject(sql, getRentalRowMapper(), bookId);
            return Optional.ofNullable(rental);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Rental> findRentalsByUserId(Long userId) {
        String sql = "select * from rentals where user_id = ?";
        try {
            return template.query(sql, getRentalRowMapper(), userId);
        } catch (DataAccessException e) {
            return List.of();
        }

    }

    @Override
    public Optional<Rental> findById(Long rental_id) {
        String sql = "select * from rentals where rental_id = ?";
        try {
            Rental rental = template.queryForObject(sql, getRentalRowMapper(), rental_id);
            return Optional.ofNullable(rental);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Rental update(Rental rental) {

        Rental oldRental = findById(rental.getRentalId())
                .orElseThrow(() -> new RentalException("존재하지 않는 대출건입니다."));

        String sql = "update rentals set " +
                "book_id = ?, " +
                "user_id = ?, " +
                "rented_at = ?, " +
                "returned_at = ? " +
                "where rental_id = ?";

        template.update(sql, rental.getBookId(), rental.getUserId(), rental.getRentedAt(), rental.getReturnedAt(), rental.getRentalId());

        return oldRental;

    }

    @Override
    public void delete(Long rental_id) {
        String sql = "delete from rentals where rental_id = ?";
        template.update(sql, rental_id);
    }

    private RowMapper<Rental> getRentalRowMapper() {
        return (rs, rowNum) -> {
            long getRentalId = rs.getLong(1);
            long getBookId = rs.getLong(2);
            long getUserId = rs.getLong(3);
            LocalDateTime getRentedAt = rs.getTimestamp(4).toLocalDateTime();
            LocalDateTime getReturnedAt = rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null;
            return new Rental(getRentalId, getBookId, getUserId, getRentedAt, getReturnedAt);
        };
    }
}

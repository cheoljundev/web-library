package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.model.Rental;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public class JdbcBookRentalRepository implements BookRentalRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcBookRentalRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("rentals")
                .usingColumns("book_id", "user_id", "rented_at")
                .usingGeneratedKeyColumns("rental_id");
    }

    @Override
    public Rental save(Rental rental) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(rental);
        Number rentalId = jdbcInsert.executeAndReturnKey(param);
        rental.setRentalId(rentalId.longValue());
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

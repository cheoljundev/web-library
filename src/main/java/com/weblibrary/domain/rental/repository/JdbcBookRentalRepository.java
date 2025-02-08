package com.weblibrary.domain.rental.repository;

import com.weblibrary.domain.rental.model.Rental;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcBookRentalRepository implements BookRentalRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcBookRentalRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
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
        String sql = "select * from rentals where book_id = :bookId and returned_at is null";
        try {
            Map<String, Long> param = Map.of("bookId", bookId);
            Rental rental = template.queryForObject(sql, param, getRentalRowMapper());
            return Optional.ofNullable(rental);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Rental> findRentalsByUserId(Long userId) {
        String sql = "select * from rentals where user_id = :userId";
        try {
            Map<String, Long> param = Map.of("userId", userId);
            return template.query(sql, param, getRentalRowMapper());
        } catch (DataAccessException e) {
            return List.of();
        }

    }

    @Override
    public Optional<Rental> findById(Long rental_id) {
        String sql = "select * from rentals where rental_id = :rental_id";
        try {
            Map<String, Long> param = Map.of("rental_id", rental_id);
            Rental rental = template.queryForObject(sql, param, getRentalRowMapper());
            return Optional.ofNullable(rental);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void update(Rental rental) {

        SqlParameterSource param = new BeanPropertySqlParameterSource(rental);

        String sql = "update rentals set " +
                "book_id = :bookId, " +
                "user_id = :userId, " +
                "rented_at = :rentedAt, " +
                "returned_at = :returnedAt " +
                "where rental_id = :rentalId";

        template.update(sql, param);


    }

    @Override
    public void delete(Long rental_id) {
        String sql = "delete from rentals where rental_id = :rental_id";
        Map<String, Long> param = Map.of("rental_id", rental_id);
        template.update(sql, param);
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

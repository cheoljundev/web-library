package com.weblibrary.infrastructure.persistence.repository.rental;

import com.weblibrary.domain.rental.exception.RentalException;
import com.weblibrary.domain.rental.model.Rental;
import com.weblibrary.domain.rental.repository.BookRentalRepository;
import com.weblibrary.domain.rental.repository.DbBookRentalRepository;
import com.weblibrary.web.connection.DBConnectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRentalRepository implements BookRentalRepository, DbBookRentalRepository {

    private final DBConnectionUtil dbConnectionUtil;

    @Override
    public Rental save(Rental rental) {
        String sql = "insert into rentals(book_id, user_id, rented_at) values(?, ?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, rental.getBookId());
            pstmt.setLong(2, rental.getUserId());
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    rental.setRentalId(rs.getLong(1));
                }
            }

            return rental;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Rental> findActiveRentalByBookId(Long bookId) {
        String sql = "select * from rentals where book_id = ? and returned_at is null";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, bookId);
            rs = pstmt.executeQuery();

            Rental rental = null;

            if (rs.next()) {
                long getRentalId = rs.getLong(1);
                long getBookId = rs.getLong(2);
                long getUserId = rs.getLong(3);
                LocalDateTime getRentedAt = rs.getTimestamp(4).toLocalDateTime();
                rental = new Rental(getRentalId, getBookId, getUserId, getRentedAt, null);
            }

            return Optional.ofNullable(rental);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

    @Override
    public List<Rental> findRentalsByUserId(Long userId) {
        String sql = "select * from rentals where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            List<Rental> rentals = new ArrayList<>();

            while (rs.next()) {
                long getRentalId = rs.getLong(1);
                long getBookId = rs.getLong(2);
                long getUserId = rs.getLong(3);
                LocalDateTime getRentedAt = rs.getTimestamp(4).toLocalDateTime();
                LocalDateTime getReturnedAt = rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null;
                Rental rental = new Rental(getRentalId, getBookId, getUserId, getRentedAt, getReturnedAt);
                rentals.add(rental);
            }

            return rentals;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

    @Override
    public Optional<Rental> findById(Long rental_id) {
        String sql = "select * from rentals where rental_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, rental_id);
            rs = pstmt.executeQuery();

            Rental rental = null;

            if (rs.next()) {
                long getRentalId = rs.getLong(1);
                long getBookId = rs.getLong(2);
                long getUserId = rs.getLong(3);
                LocalDateTime getRentedAt = rs.getTimestamp(4).toLocalDateTime();
                LocalDateTime getReturnedAt = rs.getTimestamp(5) != null ? rs.getTimestamp(5).toLocalDateTime() : null;
                rental = new Rental(getRentalId, getBookId, getUserId, getRentedAt, getReturnedAt);
            }

            return Optional.ofNullable(rental);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

    @Override
    public Rental update(Rental rental) {

        Rental oldRental = findById(rental.getRentalId())
                .orElseThrow(()->new RentalException("존재하지 않는 대출건입니다."));

        String sql = "update rentals set " +
                "book_id = ?, " +
                "user_id = ?, " +
                "rented_at = ?, " +
                "returned_at = ? " +
                "where rental_id = ?";


        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, rental.getBookId());
            pstmt.setLong(2, rental.getUserId());
            pstmt.setTimestamp(3, Timestamp.valueOf(rental.getRentedAt()));
            pstmt.setTimestamp(4, Timestamp.valueOf(rental.getReturnedAt()));
            pstmt.setLong(5, rental.getRentalId());
            pstmt.executeUpdate();

            return oldRental;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }


    }

    @Override
    public void delete(Long rental_id) {

        String sql = "delete from rentals where rental_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, rental_id);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }


    }
}

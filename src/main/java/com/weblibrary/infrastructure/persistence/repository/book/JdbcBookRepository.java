package com.weblibrary.infrastructure.persistence.repository.book;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.repository.DbBookRepository;
import com.weblibrary.web.connection.DBConnectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository, DbBookRepository {

    private final DBConnectionUtil dbConnectionUtil;

    @Override
    public Book save(Book book) {
        String sql = "insert into books(book_name, isbn) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getBookName());
            pstmt.setString(2, book.getIsbn());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    book.setBookId(rs.getLong(1));
                }
            }

            return book;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "select * from books where book_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            Book book = null;

            if (rs.next()) {
                String getBookName = rs.getString(2);
                String getIsbn = rs.getString(3);
                boolean getIsRented = rs.getBoolean(4);
                book = new Book(id, getBookName, getIsbn, getIsRented);
            }

            return Optional.ofNullable(book);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

    @Override
    public Optional<Book> findByName(String name) {
        String sql = "select * from books where book_name = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, name);
            rs = pstmt.executeQuery();

            Book book = null;

            if (rs.next()) {
                long getBookId = rs.getLong(1);
                String getIsbn = rs.getString(3);
                boolean getIsRented = rs.getBoolean(4);
                book = new Book(getBookId, name, getIsbn, getIsRented);
            }

            return Optional.ofNullable(book);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "select * from books where isbn = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();

            Book book = null;

            if (rs.next()) {
                long getBookId = rs.getLong(1);
                String getBookName = rs.getString(2);
                boolean getIsRented = rs.getBoolean(4);
                book = new Book(getBookId, getBookName, isbn, getIsRented);
            }

            return Optional.ofNullable(book);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public List<Book> findAll() {
        String sql = "select * from books";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            Book book = null;

            List<Book> books = new ArrayList<>();

            while (rs.next()) {
                long getBookId = rs.getLong(1);
                String getBookName = rs.getString(2);
                String getIsbn = rs.getString(3);
                boolean getIsRented = rs.getBoolean(4);
                book = new Book(getBookId, getBookName, getIsbn, getIsRented);
                books.add(book);
            }

            return books;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Book update(Book book) {
        Book oldBook = findById(book.getBookId())
                .orElseThrow(NotFoundBookException::new);

        String sql = "update books set " +
                "book_name = ?, " +
                "isbn = ?, " +
                "is_rented = ? " +
                "where book_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();;
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getBookName());
            pstmt.setString(2, book.getIsbn());
            pstmt.setBoolean(3, book.isRented());
            pstmt.setLong(4, book.getBookId());
            pstmt.executeUpdate();

            return oldBook;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Book> remove(Long bookId) {
        String sql = "delete from books where book_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            Book removed = findById(bookId).orElse(null);
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, bookId);
            pstmt.executeUpdate();

            return Optional.ofNullable(removed);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

}

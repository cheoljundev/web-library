package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;


@Repository
@RequiredArgsConstructor
public class JdbcBookRepository implements BookRepository {


    private final JdbcTemplate template;

    @Override
    public Book save(Book book) {
        String sql = "insert into books(book_name, isbn) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getBookName());
            pstmt.setString(2, book.getIsbn());
            return pstmt;
        }, keyHolder);
        book.setBookId(keyHolder.getKey().longValue());
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "select * from books where book_id = ?";
        try {
            Book book = template.queryForObject(sql, getBookMapper(), id);
            return Optional.ofNullable(book);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByName(String name) {
        String sql = "select * from books where book_name = ?";
        try {
            Book book = template.queryForObject(sql, getBookMapper(), name);
            return Optional.ofNullable(book);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "select * from books where isbn = ?";
        try {
            Book book = template.queryForObject(sql, getBookMapper(), isbn);
            return Optional.ofNullable(book);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Page<Book> findAll(Pageable pageable) {

        // 데이터 조회: LIMIT와 OFFSET 사용
        String sql = "select * from books order by book_id limit ? offset ?";
        List<Book> books = null;
        try {
            books = template.query(sql, getBookMapper(), pageable.getPageSize(), pageable.getOffset());
        } catch (DataAccessException e) {
            books = List.of();
        }

        // 전체 레코드 수 조회
        String countSql = "select count(*) from books";
        int total = 0;
        try {
            total = template.queryForObject(countSql, Integer.class);
        } catch (DataAccessException e) {
            total = 0;
        }

        return new PageImpl<>(books, pageable, total);
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

        template.update(sql, book.getBookName(), book.getIsbn(), book.isRented(), book.getBookId());

        return oldBook;
    }

    @Override
    public Optional<Book> remove(Long bookId) {
        String sql = "delete from books where book_id = ?";
        Book book = findById(bookId)
                .orElseThrow(NotFoundBookException::new);
        template.update(sql, bookId);
        return Optional.of(book);
    }


    private RowMapper<Book> getBookMapper() {
        return (rs, rowNum) -> {
            long bookId = rs.getLong("book_id");
            String bookName = rs.getString("book_name");
            String isbn = rs.getString("isbn");
            boolean isRented = rs.getBoolean("is_rented");
            return new Book(bookId, bookName, isbn, isRented);
        };
    }
}

package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Repository
public class JdbcBookRepository implements BookRepository {


    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcBookRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("books")
                .usingColumns("book_name", "isbn")
                .usingGeneratedKeyColumns("book_id");
    }

    @Override
    public Book save(Book book) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(book);
        Number bookId = jdbcInsert.executeAndReturnKey(param);
        book.setBookId(bookId.longValue());
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        String sql = "select * from books where book_id = :bookId";
        try {
            Map<String, Long> param = Map.of("bookId", id);
            Book book = template.queryForObject(sql, param, getBookMapper());
            return Optional.ofNullable(book);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByName(String name) {
        String sql = "select * from books where book_name = :bookName";
        try {
            Map<String, String> param = Map.of("bookName", name);
            Book book = template.queryForObject(sql, param, getBookMapper());
            return Optional.ofNullable(book);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        String sql = "select * from books where isbn = :isbn";
        try {
            Map<String, String> param = Map.of("isbn", isbn);
            Book book = template.queryForObject(sql, param, getBookMapper());
            return Optional.ofNullable(book);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Page<Book> findAll(Pageable pageable) {

        // 데이터 조회: LIMIT와 OFFSET 사용
        String sql = "select * from books order by book_id desc limit :limit offset :offset";
        List<Book> books = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("limit", pageable.getPageSize())
                    .addValue("offset", pageable.getOffset());
            books = template.query(sql, param, getBookMapper());
        } catch (DataAccessException e) {
            books = List.of();
        }

        // 전체 레코드 수 조회
        String countSql = "select count(*) from books";
        int total = 0;
        try {
            total = template.queryForObject(countSql, Map.of(), Integer.class);
        } catch (DataAccessException e) {
            total = 0;
        }

        return new PageImpl<>(books, pageable, total);
    }

    @Override
    public void update(Book book) {

        SqlParameterSource param = new BeanPropertySqlParameterSource(book);

        String sql = "update books set book_name = :bookName, " +
                "isbn = :isbn, " +
                "rented = :rented " +
                "where book_id = :bookId";

        template.update(sql, param);

    }

    @Override
    public void remove(Long bookId) {
        String sql = "delete from books where book_id = :bookId";
        Map<String, Long> param = Map.of("bookId", bookId);
        template.update(sql, param);
    }


    private RowMapper<Book> getBookMapper() {
        return (rs, rowNum) -> {
            long bookId = rs.getLong("book_id");
            String bookName = rs.getString("book_name");
            String isbn = rs.getString("isbn");
            boolean rented = rs.getBoolean("rented");
            return new Book(bookId, bookName, isbn, rented);
        };
    }
}

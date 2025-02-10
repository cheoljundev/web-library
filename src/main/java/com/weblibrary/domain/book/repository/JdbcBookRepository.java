package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JdbcBookRepository implements BookRepository {


    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcBookRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("books")
                .usingColumns("book_name", "author", "isbn")
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
    public List<Book> findAll(BookSearchCond cond, Number limit, Number offset) {

        // 데이터 조회: LIMIT와 OFFSET 사용
        StringBuilder sql = new StringBuilder("select * from books");
        List<Book> books = null;
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("limit", limit)
                .addValue("offset", offset)
                .addValue("bookName", cond.getBookName())
                .addValue("author", cond.getAuthor())
                .addValue("isbn", cond.getIsbn());

        List<String> conditions = new ArrayList<>();
        if (StringUtils.hasText(cond.getBookName())) {
            conditions.add("book_name like concat('%', :bookName, '%')");
        }
        if (StringUtils.hasText(cond.getAuthor())) {
            conditions.add("author like concat('%', :author, '%')");
        }
        if (StringUtils.hasText(cond.getIsbn())) {
            conditions.add("isbn = :isbn");
        }

        if (!conditions.isEmpty()) {
            sql.append(" where ").append(String.join(" and ", conditions));
        }

        sql.append(" order by book_id desc limit :limit offset :offset");
        try {
            books = template.query(sql.toString(), param, getBookMapper());
        } catch (DataAccessException e) {
            books = List.of();
        }

        return books;
    }

    @Override
    public int countAll(BookSearchCond cond) {

        // 데이터 조회: LIMIT와 OFFSET 사용
        StringBuilder sql = new StringBuilder("select count(*) from books");
        SqlParameterSource param = new BeanPropertySqlParameterSource(cond);

        List<String> conditions = new ArrayList<>();
        if (StringUtils.hasText(cond.getBookName())) {
            conditions.add("book_name like concat('%', :bookName, '%')");
        }
        if (StringUtils.hasText(cond.getAuthor())) {
            conditions.add("author like concat('%', :author, '%')");
        }
        if (StringUtils.hasText(cond.getIsbn())) {
            conditions.add("isbn = :isbn");
        }

        if (!conditions.isEmpty()) {
            sql.append(" where ").append(String.join(" and ", conditions));
        }

        return template.queryForObject(sql.toString(), param, Integer.class);
    }

    @Override
    public void update(Book book) {

        SqlParameterSource param = new BeanPropertySqlParameterSource(book);

        String sql = "update books set book_name = :bookName, " +
                "author = :author, " +
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
            String author = rs.getString("author");
            String isbn = rs.getString("isbn");
            boolean rented = rs.getBoolean("rented");
            return new Book(bookId, bookName, author, isbn, rented);
        };
    }
}

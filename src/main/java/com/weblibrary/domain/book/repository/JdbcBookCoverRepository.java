package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcBookCoverRepository implements BookCoverRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcBookCoverRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("book_covers")
                .usingGeneratedKeyColumns("book_cover_id");
    }

    @Override
    public BookCover save(BookCover cover) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(cover);
        Number coverId = jdbcInsert.executeAndReturnKey(param);
        cover.setBookCoverId(coverId.longValue());
        return cover;
    }

    @Override
    public Optional<BookCover> findById(Long coverId) {
        String sql = "select * from book_covers where book_cover_id = :bookCoverId";
        try {
            Map<String, Long> param = Map.of("bookCoverId", coverId);
            BookCover bookCover = template.queryForObject(sql, param, getBookCoverMapper());
            return Optional.ofNullable(bookCover);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<BookCover> findByBookId(Long bookId) {
        String sql = "select * from book_covers where book_id = :bookId";
        try {
            Map<String, Long> param = Map.of("bookId", bookId);
            BookCover bookCover = template.queryForObject(sql, param, getBookCoverMapper());
            return Optional.ofNullable(bookCover);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<BookCover> findAll() {
        String sql = "select * from book_covers";
        try {
            return template.query(sql, getBookCoverMapper());
        } catch (DataAccessException e) {
            return List.of();
        }
    }

    @Override
    public void remove(Long coverId) {
        String sql = "delete from book_covers where book_cover_id = :bookCoverId";
        Map<String, Long> param = Map.of("bookCoverId", coverId);
        template.update(sql, param);
    }

    private RowMapper<BookCover> getBookCoverMapper() {
        return (rs, rowNum) -> {
            long getBookCoverId = rs.getLong(1);
            long getBookId = rs.getLong(2);
            long getUploadFileId = rs.getLong(3);

            BookCover cover = new BookCover(getBookId, getUploadFileId);
            cover.setBookCoverId(getBookCoverId);
            return cover;
        };
    }
}

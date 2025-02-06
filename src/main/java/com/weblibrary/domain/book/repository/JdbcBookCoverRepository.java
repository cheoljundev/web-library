package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.BookCover;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookCoverRepository implements BookCoverRepository {

    private final JdbcTemplate template;

    @Override
    public void save(BookCover cover) {
        String sql = "insert into book_covers(book_id, upload_file_id) values(?, ?)";
        template.update(sql, cover.getBookId(), cover.getUploadFileId());
    }

    @Override
    public Optional<BookCover> findById(Long coverId) {
        String sql = "select * from book_covers where book_cover_id = ?";
        try {
            BookCover bookCover = template.queryForObject(sql, getBookCoverMapper(), coverId);
            return Optional.ofNullable(bookCover);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public Optional<BookCover> findByBookId(Long bookId) {
        String sql = "select * from book_covers where book_id = ?";
        try {
            BookCover bookCover = template.queryForObject(sql, getBookCoverMapper(), bookId);
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
        String sql = "delete from book_covers where book_cover_id = ?";
        template.update(sql, coverId);
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

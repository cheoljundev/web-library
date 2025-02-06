package com.weblibrary.infra.persistence.repository.book;

import com.weblibrary.domain.book.model.BookCover;
import com.weblibrary.domain.book.repository.BookCoverRepository;
import com.weblibrary.web.connection.DBConnectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcBookCoverRepository implements BookCoverRepository {

    private final DBConnectionUtil dbConnectionUtil;

    @Override
    public void save(BookCover cover) {
        String sql = "insert into book_covers(book_id, upload_file_id) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, cover.getBookId());
            pstmt.setLong(2, cover.getUploadFileId());
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

    @Override
    public Optional<BookCover> findById(Long coverId) {
        String sql = "select * from book_covers where book_cover_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, coverId);
            rs = pstmt.executeQuery();

            BookCover cover = null;

            if (rs.next()) {
                long getBookCoverId = rs.getLong(1);
                long getBookId = rs.getLong(2);
                long getUploadFileId = rs.getLong(3);

                cover = new BookCover(getBookId, getUploadFileId);
                cover.setBookCoverId(getBookCoverId);
            }

            return Optional.ofNullable(cover);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<BookCover> findByBookId(Long bookId) {
        String sql = "select * from book_covers where book_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, bookId);
            rs = pstmt.executeQuery();

            BookCover cover = null;

            if (rs.next()) {
                long getBookCoverId = rs.getLong(1);
                long getBookId = rs.getLong(2);
                long getUploadFileId = rs.getLong(3);

                cover = new BookCover(getBookId, getUploadFileId);
                cover.setBookCoverId(getBookCoverId);
            }

            return Optional.ofNullable(cover);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }

    @Override
    public List<BookCover> findAll() {
        String sql = "select * from book_covers";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            BookCover cover = null;

            List<BookCover> covers = new ArrayList<>();

            while (rs.next()) {
                long getBookCoverId = rs.getLong(1);
                long getBookId = rs.getLong(2);
                long getUploadFileId = rs.getLong(3);

                cover = new BookCover(getBookId, getUploadFileId);
                cover.setBookCoverId(getBookCoverId);
                covers.add(cover);
            }

            return covers;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public void remove(Long coverId) {
        String sql = "delete from book_covers where cover_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, coverId);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }
}

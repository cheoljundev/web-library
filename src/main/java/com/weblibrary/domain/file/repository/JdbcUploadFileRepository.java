package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.exception.NotFoundFileException;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.*;
import java.util.Optional;

import static com.weblibrary.web.connection.DBConnectionUtil.close;
import static com.weblibrary.web.connection.DBConnectionUtil.getConnection;

@Repository
@RequiredArgsConstructor
public class JdbcUploadFileRepository implements UploadFileRepository {

    private final FileStore fileStore;


    @Override
    public UploadFile save(MultipartFile multipartFile) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(multipartFile);
        String sql = "insert into upload_files(upload_file_name, store_file_name) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, uploadFile.getUploadFileName());
            pstmt.setString(2, uploadFile.getStoreFileName());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) { // 커서를 첫 번째 행으로 이동
                    Long uploadFileId = rs.getLong(1);
                    uploadFile.setUploadFileId(uploadFileId);
                }
            }

            return uploadFile;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<UploadFile> findById(Long uploadFileId) {
        String sql = "select * from upload_files where upload_file_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        UploadFile uploadFile = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, uploadFileId);
            rs = pstmt.executeQuery();

            if (rs.next()) { // 커서를 첫 번째 행으로 이동
                String getUploadFileName = rs.getString(2);
                String getStoreFileName = rs.getString(3);
                uploadFile = new UploadFile(getUploadFileName, getStoreFileName);
                uploadFile.setUploadFileId(uploadFileId);
            }

            return Optional.ofNullable(uploadFile);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }

    }

    @Override
    public void remove(Long uploadFileId) {
        findById(uploadFileId).ifPresentOrElse(uploadFile -> fileStore.deleteFile(uploadFile.getStoreFileName()), () -> {
            throw new NotFoundFileException();
        });

        String sql = "delete from upload_files where = upload_file_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {

            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, uploadFileId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }


    }
}

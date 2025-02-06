package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUploadFileRepository implements UploadFileRepository {

    private final FileStore fileStore;
    private final JdbcTemplate template;

    @Override
    public UploadFile save(MultipartFile multipartFile) {
        UploadFile uploadFile = fileStore.storeFile(multipartFile);
        String sql = "insert into upload_files(upload_file_name, store_file_name) values(?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(con -> {
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, uploadFile.getUploadFileName());
            pstmt.setString(2, uploadFile.getStoreFileName());
            return pstmt;
        }, keyHolder);
        uploadFile.setUploadFileId(keyHolder.getKey().longValue());
        return uploadFile;
    }

    @Override
    public Optional<UploadFile> findById(Long uploadFileId) {
        String sql = "select * from upload_files where upload_file_id = ?";

        try {
            UploadFile uploadFile = template.queryForObject(sql, getUploadFileMapper(), uploadFileId);
            return Optional.ofNullable(uploadFile);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public void remove(Long uploadFileId) {
        String sql = "delete from upload_files where upload_file_id = ?";
        UploadFile removed = findById(uploadFileId).orElse(null);
        template.update(sql, uploadFileId);
        fileStore.deleteFile(removed.getStoreFileName());
    }

    private RowMapper<UploadFile> getUploadFileMapper() {
        return (rs, rowNum) -> {
            UploadFile uploadFile = new UploadFile(rs.getString("upload_file_name"), rs.getString("store_file_name"));
            uploadFile.setUploadFileId(rs.getLong("upload_file_id"));
            return uploadFile;
        };
    }
}

package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.DataSource;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUploadFileRepository implements UploadFileRepository {

    private final FileStore fileStore;
    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcUploadFileRepository(FileStore fileStore, DataSource dataSource) {
        this.fileStore = fileStore;
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("upload_files")
                .usingGeneratedKeyColumns("upload_file_id");
    }

    @Override
    public UploadFile save(MultipartFile multipartFile) {
        UploadFile uploadFile = fileStore.storeFile(multipartFile);
        SqlParameterSource param = new BeanPropertySqlParameterSource(uploadFile);
        Number uploadFileId = jdbcInsert.executeAndReturnKey(param);
        uploadFile.setUploadFileId(uploadFileId.longValue());
        return uploadFile;
    }

    @Override
    public Optional<UploadFile> findById(Long uploadFileId) {
        String sql = "select * from upload_files where upload_file_id = :uploadFileId";

        try {
            Map<String, Long> param = Map.of("uploadFileId", uploadFileId);
            UploadFile uploadFile = template.queryForObject(sql, param, getUploadFileMapper());
            return Optional.ofNullable(uploadFile);
        } catch (DataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public void remove(Long uploadFileId) {
        String sql = "delete from upload_files where upload_file_id = :uploadFileId";
        UploadFile removed = findById(uploadFileId).orElse(null);
        Map<String, Long> param = Map.of("uploadFileId", uploadFileId);
        template.update(sql, param);
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

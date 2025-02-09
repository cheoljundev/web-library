package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Mapper
public interface UploadFileMapper {
    void save(UploadFile uploadFile);
    Optional<UploadFile> findById(Long uploadFileId);
    void remove(Long uploadFileId);
}

package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UploadFileRepository {
    UploadFile save(MultipartFile multipartFile);
    Optional<UploadFile> findById(Long uploadFileId);
    void remove(Long uploadFileId);
}

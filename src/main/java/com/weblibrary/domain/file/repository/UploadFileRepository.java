package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;

import java.util.List;
import java.util.Optional;

public interface UploadFileRepository {
    void save(UploadFile uploadFile);
    Optional<UploadFile> findById(Long uploadFileId);
    void remove(Long uploadFileId);
}

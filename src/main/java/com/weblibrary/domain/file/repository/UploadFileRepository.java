package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

public interface UploadFileRepository extends JpaRepository<UploadFile, Long> {
}

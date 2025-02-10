package com.weblibrary.domain.file.service;

import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadFileRepository;
import com.weblibrary.domain.file.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UploadFileService {
    private final UploadFileRepository uploadFileRepository;
    private final FileStore fileStore;

    public UploadFile save(MultipartFile multipartFile) {
        UploadFile uploadFile = fileStore.storeFile(multipartFile); // 이 안에 afterCommit이 있음
        return uploadFileRepository.save(uploadFile);
    }

    public Optional<UploadFile> findById(Long uploadFileId) {
        return uploadFileRepository.findById(uploadFileId);
    }

    public void remove(Long uploadFileId) {
        uploadFileRepository.findById(uploadFileId).ifPresent(uploadFile -> {
            uploadFileRepository.deleteById(uploadFileId);
            fileStore.deleteFile(uploadFile.getStoreFileName());
        });
    }
}

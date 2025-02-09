package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisUploadFileRepository implements UploadFileRepository {
    private final UploadFileMapper mapper;
    private final FileStore fileStore;

    @Override
    public UploadFile save(MultipartFile multipartFile) {
        UploadFile uploadFile = fileStore.storeFile(multipartFile);
        mapper.save(uploadFile);
        return uploadFile;
    }

    @Override
    public Optional<UploadFile> findById(Long uploadFileId) {
        return mapper.findById(uploadFileId);
    }

    @Override
    public void remove(Long uploadFileId) {
        mapper.findById(uploadFileId).ifPresent(uploadFile -> fileStore.deleteFile(uploadFile.getStoreFileName()));
        mapper.remove(uploadFileId);
    }
}

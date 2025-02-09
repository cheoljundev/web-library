package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Slf4j
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
        mapper.findById(uploadFileId).ifPresent(uploadFile -> {
                    mapper.remove(uploadFileId);
                    deleteFile(uploadFile);
                }
        );
    }

    private void deleteFile(UploadFile uploadFile) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                fileStore.deleteFile(uploadFile.getStoreFileName());
            }
        });
    }

}

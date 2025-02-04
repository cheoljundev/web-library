package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.exception.NotFoundFileException;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
public class MemoryUploadFileRepository implements UploadFileRepository{

    private final FileStore fileStore;
    private final Map<Long, UploadFile> store = new HashMap<>();
    private final AtomicLong uploadFileIdGenerator = new AtomicLong(1);

    @Override
    public UploadFile save(MultipartFile multipartFile) throws IOException {
        UploadFile uploadFile = fileStore.storeFile(multipartFile);
        uploadFile.setUploadFileId(uploadFileIdGenerator.getAndIncrement());
        store.put(uploadFile.getUploadFileId(), uploadFile);
        return uploadFile;
    }

    @Override
    public Optional<UploadFile> findById(Long uploadFileId) {
        return store.values().stream()
                .filter(uploadFile -> uploadFile.getUploadFileId().equals(uploadFileId))
                .findFirst();
    }

    @Override
    public void remove(Long uploadFileId) {
        findById(uploadFileId).ifPresentOrElse(uploadFile -> fileStore.deleteFile(uploadFile.getStoreFileName()), () -> {
            throw new NotFoundFileException();
        });
        store.remove(uploadFileId);
    }
}

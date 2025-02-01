package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryUploadFileRepository implements UploadFileRepository{

    private final Map<Long, UploadFile> store = new HashMap<>();
    private final AtomicLong uploadFileIdGenerator = new AtomicLong(1);

    @Override
    public void save(UploadFile uploadFile) {
        uploadFile.setUploadFileId(uploadFileIdGenerator.getAndIncrement());
        store.put(uploadFile.getUploadFileId(), uploadFile);
    }

    @Override
    public Optional<UploadFile> findById(Long uploadFileId) {
        return store.values().stream()
                .filter(uploadFile -> uploadFile.getUploadFileId().equals(uploadFileId))
                .findFirst();
    }

    @Override
    public void remove(Long uploadFileId) {
        store.remove(uploadFileId);
    }
}

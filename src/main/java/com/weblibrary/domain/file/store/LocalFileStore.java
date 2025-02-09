package com.weblibrary.domain.file.store;

import com.weblibrary.domain.file.model.UploadFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class LocalFileStore implements FileStore {

    @Value("${file.dir}")
    private String fileDir;

    private String getFullPath(String filename) {
        return fileDir + filename;
    }

    @Override
    public String getUrlPath(String filename) {
        return "file:" + getFullPath(filename);
    }

    @Override
    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                UploadFile uploadFile = storeFile(multipartFile);
                storeFileResult.add(uploadFile);
            }
        }
        return storeFileResult;
    }

    @Override
    public UploadFile storeFile(MultipartFile multipartFile) {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();

        String storeFileName = FileStore.createStoreFileName(originalFilename);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                //getfullpath : nullfillname
                try {
                    multipartFile.transferTo(new File(getFullPath(storeFileName)));
                } catch (IOException e) {
                    throw new UncheckedIOException(e);
                }
            }
        });

        return new UploadFile(originalFilename, storeFileName);
    }

    @Override
    public void deleteFile(String storeFileName) {
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                File file = new File(getFullPath(storeFileName));
                file.delete();
            }
        });
    }
}

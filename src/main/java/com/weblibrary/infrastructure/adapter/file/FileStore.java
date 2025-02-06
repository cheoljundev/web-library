package com.weblibrary.infrastructure.adapter.file;

import com.weblibrary.domain.file.model.UploadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

public interface FileStore {
    String getUrlPath(String filename);

    List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException;
    UploadFile storeFile(MultipartFile multipartFile) throws IOException;

    static String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos);
    }

    static String createStoreFileName(String originalFilename) {
        //서버에 저장하는 파일명
        String uuid = UUID.randomUUID().toString();
        //"123-123-123-123"

        String ext = extractExt(originalFilename);
        //".png"

        return uuid + ext;
        //"123.123.123.123.png"
    }

    void deleteFile(String storeFileName);
}

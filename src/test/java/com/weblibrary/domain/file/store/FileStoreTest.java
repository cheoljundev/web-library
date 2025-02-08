package com.weblibrary.domain.file.store;

import com.weblibrary.domain.file.model.UploadFile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class FileStoreTest {

    @Autowired
    FileStore fileStore;

    @Value("${file.dir}")
    private String fileDir;

    @Value("${storage.url.prefix}")
    private String urlPrefix;

    @Test
    void getUrlPath() {
        //given
        String filename = "test.txt";

        //when
        String urlPath = fileStore.getUrlPath(filename);

        //then
        assertThat(urlPath).isEqualTo(urlPrefix + fileDir + filename);
    }

    @Test
    void storeFiles() {
        //given
        MultipartFile multipartFile1 = new MockMultipartFile("test1.txt", "test1.txt", "text/plain", "hello file".getBytes());
        MultipartFile multipartFile2 = new MockMultipartFile("test2.txt", "test2.txt", "text/plain", "hello file".getBytes());

        //when
        List<UploadFile> uploadFiles = fileStore.storeFiles(List.of(multipartFile1, multipartFile2));

        //then
        assertThat(uploadFiles.size()).isEqualTo(2);

        //clean up
        fileStore.deleteFile(uploadFiles.get(0).getStoreFileName());
        fileStore.deleteFile(uploadFiles.get(1).getStoreFileName());
    }

    @Test
    void storeFile() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hello file".getBytes());

        //when
        UploadFile uploadFile = fileStore.storeFile(multipartFile);

        //then
        assertThat(uploadFile.getUploadFileName()).isEqualTo(multipartFile.getOriginalFilename());
        assertThat(uploadFile.getStoreFileName()).isNotNull();

        //clean up
        fileStore.deleteFile(uploadFile.getStoreFileName());
    }

    @Test
    void extractExt() {
        //given
        String originalFilename = "test.txt";

        //when
        String ext = FileStore.extractExt(originalFilename);

        //then
        assertThat(ext).isEqualTo(".txt");
    }

    @Test
    void createStoreFileName() {
        //given
        String originalFilename = "test.txt";

        //when
        String storeFileName = FileStore.createStoreFileName(originalFilename);

        //then
        assertThat(storeFileName).contains(".txt");
    }

    @Test
    void deleteFile_ok() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hello file".getBytes());
        UploadFile uploadFile = fileStore.storeFile(multipartFile);

        //when
        boolean result = fileStore.deleteFile(uploadFile.getStoreFileName());

        //then
        assertThat(result).isTrue();
    }

    @Test
    void deleteFile_fail() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.txt", "test.txt", "text/plain", "hello file".getBytes());
        UploadFile uploadFile = fileStore.storeFile(multipartFile);
        fileStore.deleteFile(uploadFile.getStoreFileName());

        //when
        boolean result = fileStore.deleteFile(uploadFile.getStoreFileName());

        //then
        assertThat(result).isFalse();
    }
}
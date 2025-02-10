package com.weblibrary.domain.file.service;

import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadFileRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;

@Transactional
@SpringBootTest
class UploadFileServiceTest {

    @Autowired
    UploadFileService uploadFileService;

    @Test
    void save() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());

        //when
        UploadFile saved = uploadFileService.save(multipartFile);

        //then
        assertThat(saved.getUploadFileName()).isEqualTo(multipartFile.getOriginalFilename());

        //cleanup
        uploadFileService.remove(saved.getUploadFileId());
    }

    @Test
    void findById() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile saved = uploadFileService.save(multipartFile);

        //when
        UploadFile find = uploadFileService.findById(saved.getUploadFileId()).get();

        //then
        assertThat(find).isEqualTo(saved);

        //cleanup
        uploadFileService.remove(saved.getUploadFileId());
    }

    @Test
    void remove() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile saved = uploadFileService.save(multipartFile);

        //when
        uploadFileService.remove(saved.getUploadFileId());

        //then
        assertThat(uploadFileService.findById(saved.getUploadFileId())).isEmpty();
    }
}
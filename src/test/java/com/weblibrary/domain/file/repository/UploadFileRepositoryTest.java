package com.weblibrary.domain.file.repository;

import com.weblibrary.domain.file.model.UploadFile;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UploadFileRepositoryTest {

    @Autowired
    UploadFileRepository uploadFileRepository;

    @Test
    void save() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());

        //when
        UploadFile saved = uploadFileRepository.save(multipartFile);

        //then
        assertThat(saved.getUploadFileName()).isEqualTo(multipartFile.getName());

        //cleanup
        uploadFileRepository.remove(saved.getUploadFileId());
    }

    @Test
    void findById() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile saved = uploadFileRepository.save(multipartFile);

        //when
        UploadFile find = uploadFileRepository.findById(saved.getUploadFileId()).get();

        //then
        assertThat(find).isEqualTo(saved);

        //cleanup
        uploadFileRepository.remove(saved.getUploadFileId());
    }

    @Test
    void remove() {
        //given
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile saved = uploadFileRepository.save(multipartFile);

        //when
        uploadFileRepository.remove(saved.getUploadFileId());

        //then
        assertThat(uploadFileRepository.findById(saved.getUploadFileId())).isEmpty();
    }
}
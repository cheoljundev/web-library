package com.weblibrary.domain.book.repository;

import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.BookCover;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadFileRepository;
import com.weblibrary.domain.file.service.UploadFileService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class BookCoverRepositoryTest {

    @Autowired
    BookCoverRepository bookCoverRepository;
    @Autowired
    UploadFileService uploadFileService;
    @Autowired
    BookRepository bookRepository;

    @Test
    void save() {
        //given
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile uploadFile = uploadFileService.save(multipartFile);
        BookCover bookCover = new BookCover(book.getBookId(), uploadFile.getUploadFileId());

        //when
        BookCover saved = bookCoverRepository.save(bookCover);

        //then
        assertThat(saved.getBookId()).isEqualTo(book.getBookId());

        //cleanup
        bookCoverRepository.remove(saved.getBookCoverId());
        uploadFileService.remove(uploadFile.getUploadFileId());
    }

    @Test
    void remove() {
        //given
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile uploadFile = uploadFileService.save(multipartFile);
        BookCover bookCover = new BookCover(book.getBookId(), uploadFile.getUploadFileId());
        BookCover saved = bookCoverRepository.save(bookCover);

        //when
        bookCoverRepository.remove(saved.getBookCoverId());

        //then
        assertThat(bookCoverRepository.findById(saved.getBookCoverId())).isEmpty();

        //cleanup
        uploadFileService.remove(uploadFile.getUploadFileId());
    }

    @Test
    void findById() {
        //given
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile uploadFile = uploadFileService.save(multipartFile);
        BookCover bookCover = new BookCover(book.getBookId(), uploadFile.getUploadFileId());
        BookCover saved = bookCoverRepository.save(bookCover);

        //when
        BookCover find = bookCoverRepository.findById(saved.getBookCoverId()).get();

        //then
        assertThat(find).isEqualTo(saved);

        //cleanup
        bookCoverRepository.remove(saved.getBookCoverId());
        uploadFileService.remove(uploadFile.getUploadFileId());
    }

    @Test
    void findByBookId() {
        //given
        Book book = bookRepository.save(new Book("testBook", "testAuthor", "12345"));
        MultipartFile multipartFile = new MockMultipartFile("test.jpg", "test.jpg", "image/jpg", "test data".getBytes());
        UploadFile uploadFile = uploadFileService.save(multipartFile);
        BookCover bookCover = new BookCover(book.getBookId(), uploadFile.getUploadFileId());
        BookCover saved = bookCoverRepository.save(bookCover);

        //when
        BookCover find = bookCoverRepository.findByBookId(book.getBookId()).get();

        //then
        assertThat(find).isEqualTo(saved);

        //cleanup
        bookCoverRepository.remove(saved.getBookCoverId());
        uploadFileService.remove(uploadFile.getUploadFileId());
    }

}
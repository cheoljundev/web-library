package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.DuplicateIsbnException;
import com.weblibrary.domain.book.exception.NotFoundBookCoverException;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.BookCover;
import com.weblibrary.domain.book.repository.BookCoverRepository;
import com.weblibrary.domain.book.repository.BookQueryRepository;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.repository.BookSearchCond;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.service.UploadFileService;
import com.weblibrary.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Transactional
@Slf4j
public class BookService {
    private final BookRepository bookRepository;
    private final BookQueryRepository bookQueryRepository;
    private final BookCoverRepository bookCoverRepository;
    private final UploadFileService uploadFileService;

    public Book save(NewBookForm newBookForm) {
        Book book = new Book(newBookForm.getBookName(), newBookForm.getAuthor(), newBookForm.getIsbn(), newBookForm.getDescription());
        Book savedBook = bookRepository.save(book);
        saveBookCover(savedBook, newBookForm.getCoverImage());
        return savedBook;
    }

    public void modify(ModifyBookForm form) {
        findBookById(form.getId()).ifPresentOrElse(book -> {

            if (isDuplicated(book.getIsbn(), form.getIsbn())) {
                throw new DuplicateIsbnException();
            }

            book.modify(form);
            modifyBookCover(form, book);

        }, () -> {
            throw new NotFoundBookException();
        });
    }

    public void deleteBook(Long bookId) {
        Book removedBook = bookRepository.findById(bookId)
                .orElseThrow(NotFoundBookException::new);
        removeBookCover(removedBook);
        bookRepository.delete(removedBook);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    /**
     * 책 정보를 책 ID로 조회합니다.
     *
     * @param bookId 조회할 책의 ID
     * @return 책 정보를 포함한 Optional 객체, 없으면 빈 Optional
     */
    @Transactional(readOnly = true)
    public Optional<BookInfo> findBookInfoById(Long bookId) {
        return bookRepository.findById(bookId)
                .map(book -> {
                    UploadFile image = bookCoverRepository.findByBookId(book.getBookId())
                            .flatMap(bookCover -> uploadFileService.findById(bookCover.getUploadFileId()))
                            .orElseThrow(NotFoundBookCoverException::new);
                    return new BookInfo(book.getBookId(), book.getBookName(), book.getAuthor(), book.getIsbn(), book.getDescription(), "/images/" + image.getStoreFileName());
                });
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookByName(String name) {
        return bookRepository.findByBookName(name);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Transactional(readOnly = true)
    public Optional<BookInfo> findBookInfoByBookId(Long bookId) {
        return bookRepository.findById(bookId)
                .map(book -> {
                    UploadFile image = bookCoverRepository.findByBookId(book.getBookId())
                            .flatMap(bookCover -> uploadFileService.findById(bookCover.getUploadFileId()))
                            .orElseThrow(NotFoundBookCoverException::new);
                    return new BookInfo(book.getBookId(), book.getBookName(), book.getAuthor(), book.getIsbn(), book.getDescription(), "/images/" + image.getStoreFileName());
                });
    }

    @Transactional(readOnly = true)
    public PageResponse<BookInfo> findAll(BookSearchCond cond, Pageable pageable) {
        // 페이징 처리된 책 리스트 조회
        List<Book> books = bookQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        // 전체 책 수 조회
        long total = bookQueryRepository.count(cond);

        // 각 Book을 BookInfo로 변환
        List<BookInfo> bookInfos = books.stream()
                .map(book -> {
                    UploadFile image = bookCoverRepository.findByBookId(book.getBookId())
                            .flatMap(bookCover -> uploadFileService.findById(bookCover.getUploadFileId()))
                            .orElseThrow(NotFoundBookCoverException::new);
                    return new BookInfo(book.getBookId(), book.getBookName(), book.getAuthor(), book.getIsbn(), book.getDescription(), "/images/" + image.getStoreFileName());
                })
                .collect(Collectors.toList());

        // 변환된 Page<BookInfo>를 PageResponse<BookInfo>로 변환
        PageImpl<BookInfo> bookPage = new PageImpl<>(bookInfos, pageable, total);
        return new PageResponse<>(bookInfos, bookPage.getTotalPages(), bookPage.getTotalElements(), bookPage.isFirst(), bookPage.isLast());
    }

    private void removeBookCover(Book book) {
        BookCover bookCover = bookCoverRepository.findByBookId(book.getBookId())
                .orElseThrow(NotFoundBookCoverException::new);
        bookCoverRepository.delete(bookCover);
        uploadFileService.remove(bookCover.getUploadFileId());
    }

    private void saveBookCover(Book book, MultipartFile multipartFile) {
        UploadFile image = uploadFileService.save(multipartFile);
        BookCover newBookCover = new BookCover(book.getBookId(), image.getUploadFileId());
        bookCoverRepository.save(newBookCover);
    }

    private void modifyBookCover(ModifyBookForm form, Book book) {
        if (form.getCoverImage() != null) {
            // 1. 새 이미지를 저장한다
            // 2. 기존 커버 객체를 가져온다
            // 3. 기존 커버 객체에서 uploadFileId를 변수에 임시 저장해둔다
            // 4. 기존 커버 객체의 uploadFileId를 새 이미지의 uploadFileId로 변경한다
            // 5. 기존 이미지를 삭제한다.
            UploadFile updateImage = uploadFileService.save(form.getCoverImage());
            log.debug("book={}", book);
            bookCoverRepository.findByBookId(book.getBookId())
                    .ifPresent(bookCover -> {
                        log.debug("bookCover={}", bookCover);
                        Long oldUploadFileId = bookCover.getUploadFileId();
                        bookCover.setUploadFileId(updateImage.getUploadFileId());
                        uploadFileService.remove(oldUploadFileId);
                    });
        }
    }

    private boolean isDuplicated(String oldIsbn, String newIsbn) {
        return !oldIsbn.equals(newIsbn) && findBookByIsbn(newIsbn).isPresent();
    }
}

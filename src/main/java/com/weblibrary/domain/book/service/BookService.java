package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.DuplicateIsbnException;
import com.weblibrary.domain.book.exception.NotFoundBookCoverException;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.BookCover;
import com.weblibrary.domain.book.repository.BookCoverRepository;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.repository.BookSearchCond;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
public class BookService {
    private final BookRepository bookRepository;
    private final BookCoverRepository bookCoverRepository;
    private final UploadFileRepository uploadFileRepository;

    public Book save(NewBookForm newBookForm) {
        Book book = new Book(newBookForm.getBookName(), newBookForm.getAuthor(), newBookForm.getIsbn());
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
            updateBook(book);
            modifyBookCover(form, book);

        }, () -> {
            throw new NotFoundBookException();
        });
    }

    public void deleteBook(Long bookId) {
        Book removedBook = bookRepository.findById(bookId)
                .orElseThrow(NotFoundBookException::new);
        removeBookCover(removedBook);
        bookRepository.remove(bookId);
    }

    public void updateBook(Book book) {
        bookRepository.update(book);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookByName(String name) {
        return bookRepository.findByName(name);
    }

    @Transactional(readOnly = true)
    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    @Transactional(readOnly = true)
    public Page<BookListItem> findAll(BookSearchCond cond, Pageable pageable) {
        // 페이징 처리된 책 리스트 조회
        List<Book> books = bookRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        // 전체 책 수 조회
        int total = bookRepository.countAll(cond);

        // 각 Book을 BookListItem으로 변환
        List<BookListItem> bookListItems = books.stream()
                .map(book -> {
                    UploadFile image = bookCoverRepository.findByBookId(book.getBookId())
                            .flatMap(bookCover -> uploadFileRepository.findById(bookCover.getUploadFileId()))
                            .orElseThrow(NotFoundBookCoverException::new);
                    return new BookListItem(book.getBookId(), book.getBookName(), book.getAuthor(), book.getIsbn(), image);
                })
                .collect(Collectors.toList());

        // 변환된 결과와 페이징 정보를 이용해 새로운 Page 객체 생성
        return new PageImpl<>(bookListItems, pageable, total);
    }

    private void removeBookCover(Book book) {
        BookCover bookCover = bookCoverRepository.findByBookId(book.getBookId())
                .orElseThrow(NotFoundBookCoverException::new);
        uploadFileRepository.remove(bookCover.getUploadFileId());
    }

    private void saveBookCover(Book book, MultipartFile multipartFile) {
        UploadFile image = uploadFileRepository.save(multipartFile);
        BookCover newBookCover = new BookCover(book.getBookId(), image.getUploadFileId());
        bookCoverRepository.save(newBookCover);
    }

    private void modifyBookCover(ModifyBookForm form, Book book) {
        if (form.getCoverImage() != null) {
            removeBookCover(book);
            saveBookCover(book, form.getCoverImage());
        }
    }

    private boolean isDuplicated(String oldIsbn, String newIsbn) {
        return !oldIsbn.equals(newIsbn) && findBookByIsbn(newIsbn).isPresent();
    }
}

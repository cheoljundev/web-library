package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.DuplicateIsbnException;
import com.weblibrary.domain.book.exception.NotFoundBookCoverException;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.repository.DbBookRepository;
import com.weblibrary.domain.bookCover.model.BookCover;
import com.weblibrary.domain.book.dto.BookListItem;
import com.weblibrary.domain.book.dto.ModifyBookForm;
import com.weblibrary.domain.book.dto.NewBookForm;
import com.weblibrary.domain.bookCover.repository.BookCoverRepository;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.file.exception.NotFoundFileException;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class BookService {
    private final BookRepository bookRepository;
    private final BookCoverRepository bookCoverRepository;
    private final UploadFileRepository uploadFileRepository;

    public void addBook(NewBookForm newBookForm) {
        Book book = new Book(newBookForm.getBookName(), newBookForm.getIsbn());
        Book savedBook = bookRepository.save(book);
        saveBookCover(savedBook, newBookForm.getCoverImage());
    }

    public void modifyBook(ModifyBookForm form) {
        findBookById(form.getId()).ifPresentOrElse(book -> {

            if (isDuplicated(book.getIsbn(), form.getIsbn())) {
             throw new DuplicateIsbnException();
            }

            book.modify(form.getBookName(), form.getIsbn());
            modifyBookCover(form, book);

        }, () -> {
            throw new NotFoundBookException();
        });
    }

    public void deleteBook(Long bookId) {
        bookRepository.remove(bookId).orElseThrow(NotFoundBookException::new);
        bookCoverRepository.remove(bookId);
    }

    public Book updateBook(Book book) {
        if (bookRepository instanceof DbBookRepository repository) {
            return repository.update(book);
        }

        return null;
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
    public List<BookListItem> findAll() {
        List<BookListItem> bookListItemList = new ArrayList<>();
        bookRepository.findAll().forEach((book -> {
            UploadFile image = bookCoverRepository.findByBookId(book.getBookId()).flatMap(bookCover -> uploadFileRepository.findById(bookCover.getUploadFileId()))
                    .orElseThrow(NotFoundBookCoverException::new);
            BookListItem bookListItem = new BookListItem(book.getBookId(), book.getBookName(), book.getIsbn(), image);
            bookListItemList.add(bookListItem);
        }));
        return bookListItemList;
    }

    private void removeBookCover(Book book) {
        BookCover bookCover = bookCoverRepository.findByBookId(book.getBookId())
                .orElseThrow(NotFoundBookCoverException::new);

        uploadFileRepository.findById(bookCover.getUploadFileId())
                .ifPresentOrElse(
                        uploadFile -> uploadFileRepository.remove(uploadFile.getUploadFileId()),
                        () -> {
                            throw new NotFoundFileException();
                        });

        bookCoverRepository.remove(bookCover.getBookCoverId());
    }

    private void saveBookCover(Book book, MultipartFile multipartFile) {
        try {
            UploadFile image = uploadFileRepository.save(multipartFile);
            BookCover newBookCover = new BookCover(book.getBookId(), image.getUploadFileId());
            bookCoverRepository.save(newBookCover);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
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

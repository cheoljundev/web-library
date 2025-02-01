package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.DuplicateIsbnException;
import com.weblibrary.domain.book.exception.NotFoundBookCoverException;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.bookCover.model.BookCover;
import com.weblibrary.domain.book.model.dto.BookListItem;
import com.weblibrary.domain.book.model.dto.ModifyBookForm;
import com.weblibrary.domain.book.model.dto.NewBookForm;
import com.weblibrary.domain.bookCover.repository.BookCoverRepository;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.file.exception.NotFoundFileException;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class BookService {
    private final BookRepository bookRepository;
    private final BookCoverRepository bookCoverRepository;
    private final UploadFileRepository uploadFileRepository;

    public void addBook(NewBookForm newBookForm) throws IOException {
        Book book = new Book(newBookForm.getBookName(), newBookForm.getIsbn());
        bookRepository.save(book);
        saveBookCover(book, newBookForm.getCoverImage());
    }

    public void modifyBook(Long bookId, ModifyBookForm form) {
        findBookById(bookId).ifPresentOrElse(book -> {

            if (isDuplicated(book.getIsbn(), form.getIsbn())) {
             throw new DuplicateIsbnException();
            }

            book.modify(form.getBookName(), form.getIsbn());
            if (form.getCoverImage() != null) {
                removeBookCover(book);
                saveBookCover(book, form.getCoverImage());
            }

        }, () -> {
            throw new NotFoundBookException();
        });
    }

    public void deleteBook(Long bookId) {
        bookRepository.remove(bookId).orElseThrow(NotFoundBookException::new);
        bookCoverRepository.remove(bookId);
    }

    public Optional<Book> findBookById(Long id) {
        return bookRepository.findById(id);
    }

    public Optional<Book> findBookByName(String name) {
        return bookRepository.findByName(name);
    }

    public Optional<Book> findBookByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn);
    }

    public List<BookListItem> findAll() {
        List<BookListItem> bookListItemList = new ArrayList<>();
        bookRepository.findAll().forEach((book -> {
            UploadFile image = bookCoverRepository.findByBookId(book.getBookId())
                    .map(BookCover::getImage)
                    .orElseThrow(NotFoundBookCoverException::new);
            BookListItem bookListItem = new BookListItem(book.getBookId(), book.getBookName(), book.getIsbn(), image);
            bookListItemList.add(bookListItem);
        }));
        return bookListItemList;
    }

    private void removeBookCover(Book book) {
        BookCover bookCover = bookCoverRepository.findByBookId(book.getBookId())
                .orElseThrow(NotFoundBookCoverException::new);

        uploadFileRepository.findById(bookCover.getImage().getUploadFileId())
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
            BookCover newBookCover = new BookCover(book, image);
            bookCoverRepository.save(newBookCover);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    private boolean isDuplicated(String oldIsbn, String newIsbn) {
        return !oldIsbn.equals(newIsbn) && findBookByIsbn(newIsbn).isPresent();
    }
}

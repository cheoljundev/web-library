package com.weblibrary.domain.book.service;

import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.BookCover;
import com.weblibrary.domain.book.model.dto.BookListItem;
import com.weblibrary.domain.book.model.dto.ModifyBookForm;
import com.weblibrary.domain.book.model.dto.NewBookForm;
import com.weblibrary.domain.book.repository.BookCoverRepository;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.store.FileStore;
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
    private final FileStore fileStore;

    public void addBook(NewBookForm newBookForm) throws IOException {
        Book book = new Book(newBookForm.getBookName(), newBookForm.getIsbn());
        bookRepository.save(book);
        saveBookCover(book.getId(), newBookForm.getCoverImage());
    }

    public void modifyBook(Long bookId, ModifyBookForm form) {
        findBookById(bookId).ifPresentOrElse(book -> {
            book.modify(form.getBookName(), form.getIsbn());
            removeBookCover(bookId);
            saveBookCover(bookId, form.getCoverImage());
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
                    BookListItem bookListItem = new BookListItem(book.getId(), book.getName(), book.getIsbn(), bookCoverRepository.findByBookId(book.getId()).getImage());
                    bookListItemList.add(bookListItem);
                }));
        return bookListItemList;
    }

    private void removeBookCover(Long bookId) {
        fileStore.deleteFile(bookCoverRepository.findByBookId(bookId).getImage().getStoreFileName());
        bookCoverRepository.remove(bookCoverRepository.findByBookId(bookId).getId());
    }

    private void saveBookCover(Long bookId, MultipartFile multipartFile) {
        try {
            UploadFile image = fileStore.storeFile(multipartFile);
            BookCover newBookCover = new BookCover(bookId, image);
            bookCoverRepository.save(newBookCover);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

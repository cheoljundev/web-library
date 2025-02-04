package com.weblibrary;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.bookCover.model.BookCover;
import com.weblibrary.domain.bookCover.repository.BookCoverRepository;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.account.dto.JoinUserForm;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestInit {
    private final AdminService adminService;
    private final AccountService accountService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BookCoverRepository bookCoverRepository;

    @PostConstruct
    private void init() {
//        initUser();
        initBook();
    }

    private void initUser() {
        accountService.join(new JoinUserForm("admin", "1111"));
        accountService.join(new JoinUserForm("user", "1111"));
        userRepository.findByUsername("admin")
                .ifPresent(admin -> {
                    adminService.setUserAsAdmin(admin.getUserId());
                });
    }

    /**
     * 메모리 리포지토리 환경에서 테스트를 위한 Book init 메서드
     */
    private void initBook() {
        Book saved1 = bookRepository.save(new Book("book1", "12345"));
        Book saved2 = bookRepository.save(new Book("book2", "45678"));
        Book saved3 = bookRepository.save(new Book("book3", "12395"));
        bookCoverRepository.save(new BookCover(saved1, new UploadFile("book1.jpg", "book1.jpg")));
        bookCoverRepository.save(new BookCover(saved2, new UploadFile("book2.jpg", "book2.jpg")));
        bookCoverRepository.save(new BookCover(saved3, new UploadFile("book3.jpg", "book3.jpg")));
    }
}

package com.weblibrary;

import com.weblibrary.domain.admin.Repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.Repository.UserRoleRepository;
import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.repository.BookRepository;
import com.weblibrary.domain.book.repository.MemoryBookRepository;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.repository.MemoryUserRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AppConfig {
    @Getter
    private static final AppConfig instance = new AppConfig();

    private final UserRepository userRepository = new MemoryUserRepository();
    private final UserRoleRepository userRoleRepository = new MemoryUserRoleRepository();
    private final BookRepository bookRepository = new MemoryBookRepository();
    private final UserService userService = new UserService(
            userRepository(),
            userRoleRepository()
    );
    private final BookService bookService = new BookService(bookRepository());
    private final AdminService adminService = new AdminService(
            userRepository(),
            userRoleRepository(),
            bookService()
    );

    public UserRepository userRepository() {
        return userRepository;
    }

    public UserService userService() {
        return userService;
    }

    public UserRoleRepository userRoleRepository() {
        return userRoleRepository;
    }

    public AdminService adminService() {
        return adminService;
    }

    public BookRepository bookRepository() {
        return bookRepository;
    }

    public BookService bookService() {
        return bookService;
    }
}

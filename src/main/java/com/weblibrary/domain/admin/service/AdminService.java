package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.admin.Repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.Repository.UserRoleRepository;
import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookInfo;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import static com.weblibrary.domain.admin.model.RoleType.Admin;
import static com.weblibrary.domain.admin.model.RoleType.Default;

@RequiredArgsConstructor
/**
 * 관리자 서비스 계층
 */
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BookService bookService;

    public boolean setUserAsAdmin(User user) {
        Role findAdminRole = userRoleRepository.findRoleByUserIdAndRoleType(user.getId(), Admin);
        if (findAdminRole == null) {
            Role newRole = new Role(MemoryUserRoleRepository.lastId++, user.getId(), Admin);
            userRoleRepository.save(newRole);
            return true;
        }
        return false;
    }

    public boolean setUserAsDefault(User user) {
        Role findAdminRole = userRoleRepository.findRoleByUserIdAndRoleType(user.getId(), Admin);
        Role findDefaltRole = userRoleRepository.findRoleByUserIdAndRoleType(user.getId(), Default);

        if (findAdminRole != null) {
            userRoleRepository.remove(findAdminRole.getId());
        }

        if (findDefaltRole == null) {
            Role newRole = new Role(MemoryUserRoleRepository.lastId++, user.getId(), Default);
            userRoleRepository.save(newRole);
            return true;
        }

        return false;
    }

    public User deleteUser(User user) {
        return userRepository.remove(user);
    }

    public void addBook(Book book) {
        bookService.addBook(book);
    }

    public Book deleteBook(Book book) {
        return bookService.deleteBook(book);
    }

    public Book modifyBook(Book book, ModifyBookInfo newBookInfo) {
        return book.modify(newBookInfo);
    }

    public boolean isAdmin(User user) {
        Role adminRole = userRoleRepository.findRoleByUserIdAndRoleType(user.getId(), Admin);
        return adminRole != null;
    }
}

package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.repository.UserRoleRepository;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookInfo;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.weblibrary.domain.admin.model.RoleType.Admin;

@RequiredArgsConstructor
/**
 * 관리자 서비스 계층
 */
public class AdminService {

    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final BookService bookService;

    public boolean setUserAsAdmin(Long userId) {
        // 이미 관리자 일 경우에는 실패
        if (isAdmin(userId)) {
            return false;
        }

        // 관리자 권한 생성해서 추가하기
        Role newRole = new Role(MemoryUserRoleRepository.lastId++, userId, Admin);
        userRoleRepository.save(newRole);
        return true;
    }

    public boolean setUserAsDefault(Long userId) {
        // 이미 일반 유저일 경우 실패
        if (!isAdmin(userId)) {
            return false;
        }

        // 관리자일 경우에는 관리자 권한을 삭제
        Role findAdminRole = userRoleRepository.findRoleByUserIdAndRoleType(userId, Admin);
        userRoleRepository.remove(findAdminRole.getUserId());

        return true;
    }

    public User deleteUser(Long userId) {
        return userRepository.remove(userId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
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

    public boolean isAdmin(Long userId) {
        Role adminRole = userRoleRepository.findRoleByUserIdAndRoleType(userId, Admin);
        return adminRole != null;
    }
}

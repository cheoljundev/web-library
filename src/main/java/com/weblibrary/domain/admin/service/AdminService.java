package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;
import com.weblibrary.domain.admin.repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.repository.UserRoleRepository;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookDto;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.weblibrary.domain.admin.model.RoleType.ADMIN;

@RequiredArgsConstructor
/**
 * 관리자 서비스 계층
 */
@Service
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
        Role newRole = new Role(MemoryUserRoleRepository.lastId++, userId, ADMIN);
        userRoleRepository.save(newRole);
        return true;
    }

    public RoleType findUserRoleType(Long userId) {
        List<Role> roles = userRoleRepository.findByUserId(userId);

        /* 가장 높은 권한 순서로 sort <- Comparable<Role> */
        roles.sort(null);

         /* 첫번째 (가장 높은 권한을 반환) Role의 RoleType 반환 */
        return roles.get(0).getRoleType();
    }

    public boolean setUserAsDefault(Long userId) {
        // 이미 일반 유저일 경우 실패
        if (!isAdmin(userId)) {
            return false;
        }

        // 관리자일 경우에는 관리자 권한을 삭제
        Role findAdminRole = userRoleRepository.findRoleByUserIdAndRoleType(userId, ADMIN);
        userRoleRepository.remove(findAdminRole.getId());

        return true;
    }

    public User deleteUser(Long userId) {
        return userRepository.remove(userId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void addBook(NewBookDto newBookDto) {
        bookService.addBook(newBookDto);
    }

    public Book deleteBook(Long bookId) {
        return bookService.deleteBook(bookId);
    }

    public Book modifyBook(Long bookId, ModifyBookDto modifyBookDto) {
        return bookService.findBookById(bookId).modify(modifyBookDto);
    }

    public boolean isAdmin(Long userId) {
        Role adminRole = userRoleRepository.findRoleByUserIdAndRoleType(userId, ADMIN);
        return adminRole != null;
    }
}

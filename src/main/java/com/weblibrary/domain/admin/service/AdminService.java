package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;
import com.weblibrary.domain.admin.repository.MemoryUserRoleRepository;
import com.weblibrary.domain.admin.repository.UserRoleRepository;
import com.weblibrary.domain.book.exception.NotFoundBookException;
import com.weblibrary.domain.book.model.Book;
import com.weblibrary.domain.book.model.dto.ModifyBookForm;
import com.weblibrary.domain.book.service.BookService;
import com.weblibrary.domain.file.model.UploadFile;
import com.weblibrary.domain.file.repository.UploadRepository;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.List;
import java.util.Optional;

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
    private final UploadRepository uploadRepository;

    public boolean setUserAsAdmin(Long userId) {
        if (isAdmin(userId)) {
            return false; // 이미 관리자인 경우
        }
        return addAdminRole(userId); // 관리자가 아닌 경우에만 권한 추가
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

        return removeAdminRole(userId);
    }

    private boolean removeAdminRole(Long userId) {
        return userRoleRepository.findRoleByUserIdAndRoleType(userId, ADMIN)
                .map(role -> {
                    userRoleRepository.remove(role.getId());
                    return true;
                }).orElse(false);
    }

    private boolean addAdminRole(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    Role adminRole = new Role(
                            MemoryUserRoleRepository.incrementLastId(),
                            user.getId(),
                            ADMIN
                    );
                    userRoleRepository.save(adminRole);
                    return true;
                })
                .orElse(false); // userId에 해당하는 사용자가 없으면 false 반환
    }

    public Optional<User> deleteUser(Long userId) {
        return userRepository.remove(userId);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    //TODO: coverImage는 다른 Repository에 넣는 게 좋겠다.
    public void modifyBook(Long bookId, ModifyBookForm form) {
        bookService.findBookById(bookId).map(book -> {
                UploadFile coverImage = null;
                if (form.getCoverImage() != null) {
                    try {
                        coverImage = uploadRepository.storeFile(form.getCoverImage());
                        String oldFileName = book.getCoverImage().getStoreFileName();
                        uploadRepository.deleteFile(oldFileName);
                    } catch (IOException e) {
                        throw new UncheckedIOException(e);
                    }
                }
                Book newBook = new Book(form.getBookName(), form.getIsbn(), coverImage);
                return book.modify(newBook);
        }).orElseThrow(NotFoundBookException::new);
    }

    public boolean isAdmin(Long userId) {
        return userRoleRepository.findRoleByUserIdAndRoleType(userId, ADMIN).isPresent();
    }
}

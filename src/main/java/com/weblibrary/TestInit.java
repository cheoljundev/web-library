package com.weblibrary;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.dto.NewBookForm;
import com.weblibrary.domain.user.dto.JoinUserDto;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TestInit {
    private final AdminService adminService;
    private final UserService userService;
    private final UserRepository userRepository;

    @PostConstruct
    private void init() {
        initUser();
        initBook();
    }

    private void initUser() {
        userService.join(new JoinUserDto("admin", "1111"));
        userService.join(new JoinUserDto("user", "1111"));
        userRepository.findByUsername("admin")
                .ifPresent(admin -> {
                    adminService.setUserAsAdmin(admin.getId());
                });
    }

    /**
     * 메모리 리포지토리 환경에서 테스트를 위한 Book init 메서드
     */
    private void initBook() {
//        NewBookForm jpa = new NewBookForm("JPA", "12345");
//        NewBookForm spring = new NewBookForm("SPRING", "45678");
//        NewBookForm kor = new NewBookForm("KOREAN", "72347982");
//        NewBookForm en = new NewBookForm("ENGLISH", "590328402");
//        adminService.addBook(jpa);
//        adminService.addBook(spring);
//        adminService.addBook(kor);
//        adminService.addBook(en);
    }
}

package com.weblibrary;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.dto.NewBookDto;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@ServletComponentScan
@SpringBootApplication
@RequiredArgsConstructor
public class WebLibraryApplication {

    private final AdminService adminService;
    private final UserService userService;
    private final UserRepository userRepository;

    public static void main(String[] args) {
        SpringApplication.run(WebLibraryApplication.class, args);
    }


    @PostConstruct
    private void initUser() {
        userService.join("admin", "1111");
        userService.join("user", "1111");
        User admin = userRepository.findByUsername("admin");
        adminService.setUserAsAdmin(admin.getId());
    }

    /**
     * 메모리 리포지토리 환경에서 테스트를 위한 Book init 메서드
     */
    @PostConstruct
    private void initBook() {
        NewBookDto jpa = new NewBookDto("JPA", "12345");
        NewBookDto spring = new NewBookDto("SPRING", "45678");
        adminService.addBook(jpa);
        adminService.addBook(spring);
    }
}

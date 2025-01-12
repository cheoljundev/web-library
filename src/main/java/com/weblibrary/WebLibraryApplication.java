package com.weblibrary;

import com.weblibrary.domain.admin.service.AdminService;
import com.weblibrary.domain.book.model.dto.NewBookInfo;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

//@ServletComponentScan
@SpringBootApplication
public class WebLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebLibraryApplication.class, args);
        initUser();
        initBook();
    }


    private static void initUser() {
        AppConfig appConfig = AppConfig.getInstance();
        UserService userService = appConfig.userService();
        AdminService adminService = appConfig.adminService();
        UserRepository userRepository = appConfig.userRepository();

        userService.join("admin", "1111");
        userService.join("user", "1111");
        User admin = userRepository.findByUsername("admin");
        adminService.setUserAsAdmin(admin.getId());
    }

    /**
     * 메모리 리포지토리 환경에서 테스트를 위한 Book init 메서드
     */
    private static void initBook() {
        AppConfig appConfig = AppConfig.getInstance();
        AdminService adminService = appConfig.adminService();
        NewBookInfo jpa = new NewBookInfo("JPA", "12345");
        NewBookInfo spring = new NewBookInfo("SPRING", "45678");
        adminService.addBook(jpa);
        adminService.addBook(spring);
    }
}

package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.account.dto.JoinUserForm;
import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;
    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        accountService.join(new JoinUserForm("tester", "1234"));
    }

    @AfterEach
    void tearDown() {
        userRepository.findByUsername("tester").ifPresent( user ->
                accountService.deleteUser(user.getUserId())
        );
    }

    @Test
    void save() {
        User user = new User("tester2", "1234");
        User savedUser = userRepository.save(user);
        assertThat(user).isEqualTo(savedUser);
        userRepository.remove(savedUser.getUserId());
    }

    @Test
    void findById() {
        Long userId = userRepository.findByUsername("tester").get().getUserId();
        User user = userRepository.findById(userId).orElse(null);
        assertThat(user.getUsername()).isEqualTo("tester");
    }

    @Test
    void findByUsername() {
        String username = "tester";
        User user = userRepository.findByUsername(username).get();
        assertThat(user.getUsername()).isEqualTo(username);
    }

    @Test
    void findAll() {
        //todo: 이 경우만 현재 DB에 의존적임.
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(4);
    }

    @Test
    void remove() {
        User user = userRepository.findByUsername("tester").get();
        accountService.deleteUser(user.getUserId());
        User findUser = userService.findByUsername("tester").orElse(null);
        assertThat(findUser).isNull();
    }

    @Test
    void remove_fail() {
        User removed = userRepository.remove(99L).orElse(null);
        assertThat(removed).isNull();
    }

}
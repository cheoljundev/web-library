package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.account.dto.JoinUserForm;
import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;
    @Autowired
    AccountService accountService;


    @Test
    void save() {
        User user = new User("userA", "1234");
        User savedUser = userRepository.save(user);

        assertThat(user).isEqualTo(savedUser);
    }

    @Test
    void findById() {
        Long userId = 5L;
        User user = userRepository.findById(userId).get();
        assertThat(user.getUsername()).isEqualTo("user");
    }

    @Test
    void findByUsername() {
        String username = "user";
        User user = userRepository.findByUsername("user").get();
        assertThat(user.getUsername()).isEqualTo("user");
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();
        assertThat(users.size()).isEqualTo(3);
    }

    @Test
    void remove() {
        accountService.join(new JoinUserForm("tester", "1234"));
        User user = userRepository.findByUsername("tester").get();
        User removed = userRepository.remove(user.getUserId()).get();
        assertThat(removed).isEqualTo(user);
    }

    @Test
    void clearAll() {
    }
}
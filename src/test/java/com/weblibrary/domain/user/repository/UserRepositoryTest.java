package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;


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
    }

    @Test
    void clearAll() {
    }
}
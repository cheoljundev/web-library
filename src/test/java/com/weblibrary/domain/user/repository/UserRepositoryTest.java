package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
    }

    @Test
    void findByUsername() {
    }

    @Test
    void findAll() {
    }

    @Test
    void remove() {
    }

    @Test
    void clearAll() {
    }
}
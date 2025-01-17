package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @AfterEach
    void afterEach() {
        userRepository.clearAll();
    }

    @Test
    void findById() {
        // given
        User user = new User(1L, "user", "12345");
        userRepository.save(user);
        //when
        User findUser = userRepository.findById(1L);
        //then
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void findByUsername() {
        // given
        User user = new User(1L, "user", "12345");
        userRepository.save(user);
        //when
        User findUser = userRepository.findByUsername("user");
        //then
        assertThat(findUser).isEqualTo(user);
    }

    @Test
    void findAll() {
        // given
        User user1 = new User(1L, "user1", "12345");
        User user2 = new User(2L, "user2", "12345");
        userRepository.save(user1);
        userRepository.save(user2);
        //when
        List<User> users = userRepository.findAll();
        //then
        assertThat(users.size()).isEqualTo(2);
    }
}
package com.weblibrary.domain.user.service;

import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void save() {
        //given
        User tester = new User("tester", "1234");

        //when
        userService.save(tester);

        //then
        User findUser = userService.findByUsername(tester.getUsername()).get();
        assertEquals(findUser, tester);
    }

    @Test
    void findByUsername() {
        //given
        User tester = new User("tester", "1234");
        userService.save(tester);

        //when
        User findUser = userService.findByUsername(tester.getUsername()).get();

        //then
        assertEquals(findUser, tester);
    }

    @Test
    void findById() {
        //given
        User saved = userService.save(new User("tester", "1234"));

        //when
        User findUser = userService.findById(saved.getUserId()).get();

        //then
        assertEquals(findUser, saved);
    }

    @Test
    void update() {
        //given
        User saved = userService.save(new User("tester", "1234"));
        User updateUser = new User("tester", "5678");
        updateUser.setUserId(saved.getUserId());

        //when
        userService.update(updateUser);

        //then
        User updatedUser = userService.findById(updateUser.getUserId()).get();
        assertEquals(updatedUser.getPassword(), "5678");
    }
}
package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        //given
        User tester = new User("tester", "1234");

        //when
        User saved = userRepository.save(tester);

        //then
        User findUser = userRepository.findById(saved.getUserId()).get();
        assertThat(findUser).isEqualTo(saved);
    }

    @Test
    void findById() {
        //given
        User tester = new User("tester", "1234");
        User saved = userRepository.save(tester);

        //when
        User findUser = userRepository.findById(saved.getUserId()).get();

        //then
        assertThat(findUser).isEqualTo(saved);
    }

    @Test
    void findByUsername() {
        //given
        User tester = new User("tester", "1234");
        User saved = userRepository.save(tester);

        //when
        User findUser = userRepository.findByUsername(saved.getUsername()).get();

        //then
        assertThat(findUser).isEqualTo(saved);
    }

    @Test
    void findAll() {
        //given
        userRepository.save(new User("user1", "1234"));
        userRepository.save(new User("user2", "1234"));
        userRepository.save(new User("user3", "1234"));
        userRepository.save(new User("user4", "1234"));
        userRepository.save(new User("user5", "1234"));
        userRepository.save(new User("user6", "1234"));
        userRepository.save(new User("user7", "1234"));
        userRepository.save(new User("user8", "1234"));
        userRepository.save(new User("user9", "1234"));
        userRepository.save(new User("user10", "1234"));
        userRepository.save(new User("user11", "1234"));
        userRepository.save(new User("user12", "1234"));

        //when
        Pageable pageable = PageRequest.of(0, 10);
        List<User> page = userRepository.findAll(pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(page.size()).isEqualTo(10);
    }

    @Test
    void countAll() {
        //given
        userRepository.save(new User("user1", "1234"));
        userRepository.save(new User("user2", "1234"));
        userRepository.save(new User("user3", "1234"));
        userRepository.save(new User("user4", "1234"));
        userRepository.save(new User("user5", "1234"));
        userRepository.save(new User("user6", "1234"));
        userRepository.save(new User("user7", "1234"));
        userRepository.save(new User("user8", "1234"));
        userRepository.save(new User("user9", "1234"));
        userRepository.save(new User("user10", "1234"));
        userRepository.save(new User("user11", "1234"));
        userRepository.save(new User("user12", "1234"));

        //when
        int total = userRepository.countAll();

        //then
        assertThat(total).isEqualTo(12);
    }

    @Test
    void remove() {
        //given
        User tester = new User("tester", "1234");
        User saved = userRepository.save(tester);

        //when
        userRepository.remove(saved.getUserId());

        //then
        assertThat(userRepository.findById(saved.getUserId())).isEmpty();
    }

    @Test
    void update() {
        //given
        User tester = new User("tester", "1234");
        User saved = userRepository.save(tester);

        //when
        User updateUser = new User("update", "1234");
        updateUser.setUserId(saved.getUserId());
        userRepository.update(updateUser);

        //then
        User findUser = userRepository.findById(saved.getUserId()).get();
        assertThat(findUser.getUsername()).isEqualTo(updateUser.getUsername());
    }
}
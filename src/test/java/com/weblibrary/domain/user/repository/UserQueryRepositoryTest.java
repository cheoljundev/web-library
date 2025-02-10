package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class UserQueryRepositoryTest {

    @Autowired
    UserQueryRepository userQueryRepository;

    @Autowired
    UserRepository userRepository;

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
        List<User> page = userQueryRepository.findAll(pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(page.size()).isEqualTo(10);
    }

    @Test
    void count() {
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
        long total = userQueryRepository.count();

        //then
        assertThat(total).isEqualTo(12);
    }
}
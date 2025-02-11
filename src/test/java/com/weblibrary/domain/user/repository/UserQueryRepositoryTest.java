package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.service.UserService;
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

    @Autowired
    AccountService accountService;
    @Autowired
    private UserService userService;

    @Test
    void findAll_no_cond() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        //when
        Pageable pageable = PageRequest.of(0, 10);
        UserSearchCond cond = new UserSearchCond();
        List<User> page = userQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(page.size()).isEqualTo(10);
    }

    @Test
    void findAll_cond_username() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        User target = accountService.join(new JoinUserForm("target", "1234"));

        //when
        Pageable pageable = PageRequest.of(0, 10);
        UserSearchCond cond = new UserSearchCond(target.getUsername(), null);
        List<User> page = userQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(page.size()).isEqualTo(1);
        assertThat(page.get(0).getUsername()).isEqualTo(target.getUsername());

    }

    @Test
    void findAll_cond_roleType() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        User admin1 = accountService.join(new JoinUserForm("admin1", "1234"));
        User admin2 = accountService.join(new JoinUserForm("admin2", "1234"));
        userService.setUserAsAdmin(admin1.getUserId());
        userService.setUserAsAdmin(admin2.getUserId());

        //when
        Pageable pageable = PageRequest.of(0, 10);
        UserSearchCond cond = new UserSearchCond(null, RoleType.ADMIN);
        List<User> page = userQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(page.size()).isEqualTo(2);
    }

    @Test
    void findAll_cond_and() {
        //given
        for (int i = 0; i < 15; i++) {
            userRepository.save(new User("tester" + i, "1234"));
        }

        User admin1 = accountService.join(new JoinUserForm("admin1", "1234"));
        User admin2 = accountService.join(new JoinUserForm("admin2", "1234"));
        userService.setUserAsAdmin(admin1.getUserId());
        userService.setUserAsAdmin(admin2.getUserId());

        //when
        Pageable pageable = PageRequest.of(0, 10);
        UserSearchCond cond = new UserSearchCond(admin1.getUsername(), RoleType.ADMIN);
        List<User> page = userQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());

        //then
        assertThat(page.size()).isEqualTo(1);
        assertThat(page.get(0).getUsername()).isEqualTo(admin1.getUsername());
    }

    @Test
    void count_no_cond() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        //when
        UserSearchCond cond = new UserSearchCond();
        long total = userQueryRepository.count(cond);

        //then
        assertThat(total).isEqualTo(15);
    }

    @Test
    void count_cond_username() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        User target = accountService.join(new JoinUserForm("target", "1234"));


        //when
        UserSearchCond cond = new UserSearchCond(target.getUsername(), null);
        long total = userQueryRepository.count(cond);

        //then
        assertThat(total).isEqualTo(1);
    }

    @Test
    void count_cond_roleType() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }


        User admin1 = accountService.join(new JoinUserForm("admin1", "1234"));
        User admin2 = accountService.join(new JoinUserForm("admin2", "1234"));
        userService.setUserAsAdmin(admin1.getUserId());
        userService.setUserAsAdmin(admin2.getUserId());


        //when
        UserSearchCond cond = new UserSearchCond(null, RoleType.ADMIN);
        long total = userQueryRepository.count(cond);

        //then
        assertThat(total).isEqualTo(2);
    }

    @Test
    void count_cond_and() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        User admin1 = accountService.join(new JoinUserForm("admin1", "1234"));
        User admin2 = accountService.join(new JoinUserForm("admin2", "1234"));
        userService.setUserAsAdmin(admin1.getUserId());
        userService.setUserAsAdmin(admin2.getUserId());

        //when
        UserSearchCond cond = new UserSearchCond(admin1.getUsername(), RoleType.ADMIN);
        long total = userQueryRepository.count(cond);

        //then
        assertThat(total).isEqualTo(1);
    }
}
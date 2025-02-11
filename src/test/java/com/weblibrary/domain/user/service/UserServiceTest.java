package com.weblibrary.domain.user.service;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserSearchCond;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    AccountService accountService;

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
    void findById() {
        //given
        User saved = userService.save(new User("tester", "1234"));

        //when
        User findUser = userService.findById(saved.getUserId()).get();

        //then
        assertEquals(findUser, saved);
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
    void findAll_no_cond() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        //when
        Pageable pageable = PageRequest.of(0, 10);
        UserSearchCond cond = new UserSearchCond();
        Page<UserInfo> userPage = userService.findAll(cond, pageable);

        //then
        assertThat(userPage.getContent().size()).isEqualTo(10);
        assertThat(userPage.getTotalElements()).isEqualTo(15);
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
        Page<UserInfo> userPage = userService.findAll(cond, pageable);

        //then
        assertThat(userPage.getContent().size()).isEqualTo(1);
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getTotalPages()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getUsername()).isEqualTo(target.getUsername());
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
        Page<UserInfo> userPage = userService.findAll(cond, pageable);

        //then
        assertThat(userPage.getContent().size()).isEqualTo(2);
        assertThat(userPage.getTotalElements()).isEqualTo(2);
        assertThat(userPage.getTotalPages()).isEqualTo(1);
    }

    @Test
    void findAll_cond_and() {
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
        UserSearchCond cond = new UserSearchCond(admin1.getUsername(), RoleType.ADMIN);
        Page<UserInfo> userPage = userService.findAll(cond, pageable);
        //then
        assertThat(userPage.getContent().size()).isEqualTo(1);
        assertThat(userPage.getTotalElements()).isEqualTo(1);
        assertThat(userPage.getTotalPages()).isEqualTo(1);
        assertThat(userPage.getContent().get(0).getUsername()).isEqualTo(admin1.getUsername());
    }

    @Test
    void setUserAsAdmin() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        //when
        userService.setUserAsAdmin(tester.getUserId());

        //then
        boolean isAdmin = userService.isAdmin(tester.getUserId());
        assertThat(isAdmin).isTrue();

    }

    @Test
    void findUserRoleType() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        userService.setUserAsAdmin(tester.getUserId());

        //when
        RoleType roleType = userService.findUserRoleType(tester.getUserId());

        //then
        assertThat(roleType).isEqualTo(RoleType.ADMIN);

    }

    @Test
    void setUserAsDefault() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        userService.setUserAsAdmin(tester.getUserId());

        //when
        userService.setUserAsDefault(tester.getUserId());

        //then
        boolean isAdmin = userService.isAdmin(tester.getUserId());
        assertThat(isAdmin).isFalse();
    }

    @Test
    void isAdmin() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        userService.setUserAsAdmin(tester.getUserId());

        //when
        boolean admin = userService.isAdmin(tester.getUserId());

        //then
        assertThat(admin).isTrue();
    }

}
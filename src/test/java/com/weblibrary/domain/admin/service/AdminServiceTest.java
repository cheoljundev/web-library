package com.weblibrary.domain.admin.service;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class AdminServiceTest {

    @Autowired
    AdminService adminService;
    @Autowired
    AccountService accountService;

    @Test
    void setUserAsAdmin() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        //when
        adminService.setUserAsAdmin(tester.getUserId());

        //then
        boolean isAdmin = adminService.isAdmin(tester.getUserId());
        assertThat(isAdmin).isTrue();

    }

    @Test
    void findUserRoleType() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        adminService.setUserAsAdmin(tester.getUserId());

        //when
        RoleType roleType = adminService.findUserRoleType(tester.getUserId());

        //then
        assertThat(roleType).isEqualTo(RoleType.ADMIN);

    }

    @Test
    void setUserAsDefault() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        adminService.setUserAsAdmin(tester.getUserId());

        //when
        adminService.setUserAsDefault(tester.getUserId());

        //then
        boolean isAdmin = adminService.isAdmin(tester.getUserId());
        assertThat(isAdmin).isFalse();
    }

    @Test
    void findAllUsers() {
        //given
        for (int i = 0; i < 15; i++) {
            accountService.join(new JoinUserForm("tester" + i, "1234"));
        }

        //when
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserInfo> userPage = adminService.findAllUsers(pageable);

        //then
        assertThat(userPage.getContent().size()).isEqualTo(10);
        assertThat(userPage.getTotalElements()).isEqualTo(15);
    }

    @Test
    void isAdmin() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        adminService.setUserAsAdmin(tester.getUserId());

        //when
        boolean admin = adminService.isAdmin(tester.getUserId());

        //then
        assertThat(admin).isTrue();
    }
}
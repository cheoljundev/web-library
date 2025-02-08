package com.weblibrary.domain.account.service;

import com.weblibrary.domain.account.exception.InvalidLoginException;
import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    void join() {
        //given
        JoinUserForm form = new JoinUserForm("tester", "1234");

        //when
        User joined = accountService.join(form);

        //then
        assertThat(joined.getUserId()).isNotNull();
        assertThat(joined.getUsername()).isEqualTo(form.getUsername());

    }

    @Test
    void login_ok() {
        //given
        accountService.join(new JoinUserForm("tester", "1234"));

        //when
        MockHttpSession session = new MockHttpSession();
        LoginUserForm loginUserForm = new LoginUserForm();
        loginUserForm.setUsername("tester");
        loginUserForm.setPassword("1234");
        accountService.login(session, loginUserForm);
    }
    @Test
    @DisplayName("로그인 실패 : 아이디 불일치")
    void login_fail_1() {
        //given
        accountService.join(new JoinUserForm("tester", "1234"));

        //when
        MockHttpSession session = new MockHttpSession();
        LoginUserForm loginUserForm = new LoginUserForm();
        loginUserForm.setUsername("admin");
        loginUserForm.setPassword("1234");
        assertThatThrownBy(() -> accountService.login(session, loginUserForm))
                .isInstanceOf(InvalidLoginException.class);
    }

    @Test
    @DisplayName("로그인 실패 : 비밀번호 불일치")
    void login_fail_2() {
        //given
        accountService.join(new JoinUserForm("tester", "1234"));

        //when
        MockHttpSession session = new MockHttpSession();
        LoginUserForm loginUserForm = new LoginUserForm();
        loginUserForm.setUsername("tester");
        loginUserForm.setPassword("12345");
        assertThatThrownBy(() -> accountService.login(session, loginUserForm))
                .isInstanceOf(InvalidLoginException.class);
    }

    @Test
    void deleteUser() {
        //given
        User user = accountService.join(new JoinUserForm("tester", "1234"));

        //when
        accountService.deleteUser(user.getUserId());

        //then
        assertThatThrownBy(() -> accountService.deleteUser(user.getUserId()))
                .isInstanceOf(NotFoundUserException.class);
    }
}
package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired
    AccountService accountService;

    @Test
    void save() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        Role role = new Role(tester.getUserId(), RoleType.ADMIN);

        //when
        userRoleRepository.save(role);

        //then
        List<Role> roles = userRoleRepository.findRolesByUserId(tester.getUserId());
        assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        Role findByUserId = userRoleRepository.findRolesByUserId(tester.getUserId()).get(0);

        //when
        Role role = userRoleRepository.findById(findByUserId.getRoleId()).get();

        //then
        assertThat(role.getUserId()).isEqualTo(findByUserId.getUserId());
    }

    @Test
    void findRoleByUserIdAndRoleType() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        //when
        Role role = userRoleRepository.findRoleByUserIdAndRoleType(tester.getUserId(), RoleType.DEFAULT).orElse(null);

        //then
        assertThat(role.getRoleType()).isEqualTo(RoleType.DEFAULT);
    }

    @Test
    void findRolesByUserId() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        //when
        List<Role> roles = userRoleRepository.findRolesByUserId(tester.getUserId());

        //then
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    void remove() {
        //given
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        //when
        userRoleRepository.findRolesByUserId(tester.getUserId()).forEach(role -> {
            userRoleRepository.remove(role.getRoleId());
        });

        //then
        List<Role> roles = userRoleRepository.findRolesByUserId(tester.getUserId());
        assertThat(roles.size()).isEqualTo(0);
    }
}
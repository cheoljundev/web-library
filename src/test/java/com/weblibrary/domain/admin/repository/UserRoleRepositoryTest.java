package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRoleRepository;
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
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        Role role = new Role(tester.getUserId(), RoleType.ADMIN);
        userRoleRepository.save(role);

        List<Role> roles = userRoleRepository.findRolesByUserId(tester.getUserId());
        assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        Role roleById = userRoleRepository.findById(tester.getUserId()).orElse(null);
        assertThat(roleById.getUserId()).isEqualTo(tester.getUserId());
    }

    @Test
    void findRoleByUserIdAndRoleType() {
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        Role role = userRoleRepository.findRoleByUserIdAndRoleType(tester.getUserId(), RoleType.DEFAULT).orElse(null);
        assertThat(role.getRoleType()).isEqualTo(RoleType.DEFAULT);
    }

    @Test
    void findRolesByUserId() {
        User tester = accountService.join(new JoinUserForm("tester", "1234"));
        List<Role> roles = userRoleRepository.findRolesByUserId(tester.getUserId());
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    void remove() {
        User tester = accountService.join(new JoinUserForm("tester", "1234"));

        userRoleRepository.findRolesByUserId(tester.getUserId()).forEach(role -> {
            userRoleRepository.remove(role.getRoleId());
        });

        List<Role> roles = userRoleRepository.findRolesByUserId(tester.getUserId());
        assertThat(roles.size()).isEqualTo(0);
    }
}
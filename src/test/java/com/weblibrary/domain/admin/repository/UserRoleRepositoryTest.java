package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.account.service.JoinUserForm;
import com.weblibrary.domain.account.service.AccountService;
import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.repository.UserRoleRepository;
import com.weblibrary.domain.user.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserRoleRepositoryTest {

    @Autowired
    UserRoleRepository userRoleRepository;
    @Autowired AccountService accountService;
    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @BeforeEach
    void setUp() {
        accountService.join(new JoinUserForm("tester", "1234"));
    }

    @AfterEach
    void tearDown() {
        User user = userRepository.findByUsername("tester").orElse(null);
        accountService.deleteUser(user.getUserId());
    }

    @Test
    void save() {
        Long userId = userRepository.findByUsername("tester").orElse(null).getUserId();
        Role role = new Role(userId, RoleType.ADMIN);
        userRoleRepository.save(role);

        List<Role> roles = userRoleRepository.findRolesByUserId(userId);
        assertThat(roles.size()).isEqualTo(2);
    }

    @Test
    void findById() {
        User tester = userService.findByUsername("tester").orElse(null);
        Role roleByUserIdAndRoleType = userRoleRepository.findRoleByUserIdAndRoleType(tester.getUserId(), RoleType.DEFAULT).orElse(null);

        Long roleId = roleByUserIdAndRoleType.getRoleId();
        Role roleById = userRoleRepository.findById(roleId).orElse(null);

        assertThat(roleByUserIdAndRoleType).isEqualTo(roleById);

    }

    @Test
    void findRoleByUserIdAndRoleType() {
        User tester = userService.findByUsername("tester").orElse(null);
        Role role = userRoleRepository.findRoleByUserIdAndRoleType(tester.getUserId(), RoleType.DEFAULT).orElse(null);

        assertThat(role.getUserId()).isEqualTo(tester.getUserId());
    }

    @Test
    void findRolesByUserId() {
        Long testerId = userService.findByUsername("tester").orElse(null).getUserId();

        List<Role> roles = userRoleRepository.findRolesByUserId(testerId);

        assertThat(roles.size()).isEqualTo(1);

    }

    @Test
    void findAll() {
        List<Role> roles = userRoleRepository.findAll();
        assertThat(roles.size()).isEqualTo(1);
    }

    @Test
    void remove() {
        User tester = userService.findByUsername("tester").orElse(null);
        Role roleByUserIdAndRoleType = userRoleRepository.findRoleByUserIdAndRoleType(tester.getUserId(), RoleType.DEFAULT).orElse(null);
        Long roleId = roleByUserIdAndRoleType.getRoleId();
        userRoleRepository.remove(roleId);

        List<Role> roles = userRoleRepository.findAll();
        assertThat(roles.size()).isEqualTo(0);
    }
}
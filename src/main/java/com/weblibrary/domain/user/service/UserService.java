package com.weblibrary.domain.user.service;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.RoleRepository;
import com.weblibrary.domain.user.repository.UserQueryRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.repository.UserSearchCond;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.weblibrary.domain.user.model.RoleType.ADMIN;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final RoleRepository roleRepository;


    public User save(User user) {
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findByUserId(id);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional(readOnly = true)
    public Page<UserInfo> findAll(UserSearchCond cond, Pageable pageable) {
        List<User> users = userQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());
        long total = userQueryRepository.count(cond);
        List<UserInfo> userInfos = users.stream().map(user -> {
            RoleType roleType = findUserRoleType(user.getUserId());
            return UserInfo.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .roleTypeName(roleType.name())
                    .build();
        }).collect(Collectors.toList());
        return new PageImpl<>(userInfos, pageable, total);
    }

    @Transactional(readOnly = true)
    public RoleType findUserRoleType(Long userId) {
        List<Role> roles = roleRepository.findRolesByUserId(userId);

        /* 가장 높은 권한 순서로 sort <- Comparable<Role> */
        roles.sort(null);

        /* 첫번째 (가장 높은 권한을 반환) Role의 RoleType 반환 */
        return roles.get(0).getRoleType();
    }

    public boolean setUserAsAdmin(Long userId) {
        if (isAdmin(userId)) {
            return false; // 이미 관리자인 경우
        }
        return addAdminRole(userId); // 관리자가 아닌 경우에만 권한 추가
    }

    public boolean setUserAsDefault(Long userId) {
        // 이미 일반 유저일 경우 실패
        if (!isAdmin(userId)) {
            return false;
        }

        return removeAdminRole(userId);
    }

    private boolean addAdminRole(Long userId) {
        return userRepository.findByUserId(userId)
                .map(user -> {
                    Role adminRole = new Role(
                            user.getUserId(),
                            ADMIN
                    );
                    roleRepository.save(adminRole);
                    return true;
                })
                .orElse(false); // userId에 해당하는 사용자가 없으면 false 반환
    }

    private boolean removeAdminRole(Long userId) {
        return roleRepository.findRoleByUserIdAndRoleType(userId, ADMIN)
                .map(role -> {
                    roleRepository.deleteById(role.getRoleId());
                    return true;
                }).orElse(false);
    }

    @Transactional(readOnly = true)
    public boolean isAdmin(Long userId) {
        return roleRepository.findRoleByUserIdAndRoleType(userId, ADMIN).isPresent();
    }

}

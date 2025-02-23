package com.weblibrary.domain.user.service;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.model.User;
import com.weblibrary.domain.user.repository.RoleRepository;
import com.weblibrary.domain.user.repository.UserQueryRepository;
import com.weblibrary.domain.user.repository.UserRepository;
import com.weblibrary.domain.user.repository.UserSearchCond;
import com.weblibrary.web.response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.weblibrary.domain.user.model.RoleType.ADMIN;
import static com.weblibrary.domain.user.model.RoleType.DEFAULT;

/**
 * UserService 클래스는 사용자와 관련된 비즈니스 로직을 처리합니다.
 * 여기에는 사용자 정보 조회, 사용자 역할 설정 및 사용자 삭제가 포함됩니다.
 */
@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final RoleRepository roleRepository;

   /**
    * 주어진 사용자 정보를 저장합니다.
    *
    * @param user 저장할 사용자 정보
    * @return 저장된 사용자 정보
    */
   public User save(User user) {
       return userRepository.save(user);
   }

   /**
    * 주어진 ID로 사용자를 조회합니다.
    *
    * @param id 조회할 사용자 ID
    * @return 조회된 사용자 정보 (Optional)
    */
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findByUserId(id);
    }

    /**
     * 주어진 사용자 ID로 사용자 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자 정보 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<UserInfo> findUserInfoById(Long userId) {
        return userRepository.findByUserId(userId).map(user -> {
            List<RoleTypeInfo> roleTypeInfos = findUserRoleTypes(user.getUserId());
            return UserInfo.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .roles(roleTypeInfos)
                    .remainingRents(user.getRemainingRents())
                    .build();
        });
    }

    /**
     * 주어진 사용자 이름으로 사용자를 조회합니다.
     *
     * @param username 조회할 사용자 이름
     * @return 조회된 사용자 정보 (Optional)
     */
    @Transactional(readOnly = true)
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    /**
     * 주어진 조건에 따라 모든 사용자를 조회합니다.
     *
     * @param cond 조회 조건
     * @param pageable 페이지 정보
     * @return 조회된 사용자 정보 페이지 응답
     */
    @Transactional(readOnly = true)
    public PageResponse<UserInfo> findAll(UserSearchCond cond, Pageable pageable) {
        List<User> users = userQueryRepository.findAll(cond, pageable.getPageSize(), pageable.getOffset());
        long total = userQueryRepository.count(cond);
        List<UserInfo> userInfos = users.stream().map(user -> {
            List<RoleTypeInfo> roleTypeInfos = findUserRoleTypes(user.getUserId());
            return UserInfo.builder()
                    .id(user.getUserId())
                    .username(user.getUsername())
                    .roles(roleTypeInfos)
                    .remainingRents(user.getRemainingRents())
                    .build();
        }).collect(Collectors.toList());
        PageImpl<UserInfo> userPage = new PageImpl<>(userInfos, pageable, total);
        return new PageResponse<>(userInfos, userPage.getTotalPages(), userPage.getTotalElements(), userPage.isFirst(), userPage.isLast());
    }

    /**
     * 주어진 사용자 ID로 사용자의 역할 정보를 조회합니다.
     *
     * @param userId 사용자 ID
     * @return 사용자의 역할 정보 목록
     */
    @Transactional(readOnly = true)
    public List<RoleTypeInfo> findUserRoleTypes(Long userId) {
        return roleRepository.findRolesByUserId(userId).stream()
                .map(role -> {
                    RoleType roleType = role.getRoleType();
                    return new RoleTypeInfo(roleType.name(), roleType.getDescription());
                })
                .toList();

    }

    /**
     * 주어진 사용자 ID로 사용자의 역할을 설정합니다.
     *
     * @param userId 사용자 ID
     * @param roleTypes 설정할 역할 목록
     */
    public void setRoles(Long userId, List<RoleType> roleTypes) {
        roleRepository.deleteAllByUserId(userId);
        boolean hasDefault = roleTypes.contains(DEFAULT);
        if (!hasDefault) {
            roleTypes.add(DEFAULT);
        }
        roleTypes.forEach(roleType -> {
            Role role = new Role(userId, roleType);
            roleRepository.save(role);
        });
    }

    /**
     * 주어진 사용자 ID로 사용자가 관리자인지 확인합니다.
     *
     * @param userId 사용자 ID
     * @return 관리자 여부
     */
    @Transactional(readOnly = true)
    public boolean isAdmin(Long userId) {
        return roleRepository.findRoleByUserIdAndRoleType(userId, ADMIN).isPresent();
    }

}

package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    void save(Role role);
    Optional<Role> findById(Long roleId);
    Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findRolesByUserId(Long userId);
    boolean remove(Long roleId);
}

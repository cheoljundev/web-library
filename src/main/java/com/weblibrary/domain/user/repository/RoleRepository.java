package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;

import java.util.List;
import java.util.Optional;

public interface RoleRepository {
    void save(Role role);
    Optional<Role> findById(Long roleId);
    Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findRolesByUserId(Long userId);
    int remove(Long roleId);
}

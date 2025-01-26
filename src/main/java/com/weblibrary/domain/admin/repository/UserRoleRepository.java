package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    void save(Role role);
    Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findByUserId(Long userId);
    List<Role> findRolesByUserId(Long userId);
    List<Role> findAll();
    Optional<Role> remove(Long roleId);
}

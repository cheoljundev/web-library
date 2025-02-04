package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;

import java.util.List;
import java.util.Optional;

public interface UserRoleRepository {
    void save(Role role);
    Optional<Role> findById(Long roleId);
    Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findRolesByUserId(Long userId);
    List<Role> findAll();
    boolean remove(Long roleId);
}

package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;

import java.util.List;

public interface UserRoleRepository {
    void save(Role role);
    Role findRoleByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findByUserId(Long userId);
    List<Role> findRolesByUserId(Long userId);
    Role remove(Long roleId);
}

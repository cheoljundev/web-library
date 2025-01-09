package com.weblibrary.domain.admin.Repository;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;

import java.util.List;

public interface UserRoleRepository {
    void save(Role role);
    Role findTypeByUserIdAndRoleType(Long userId, RoleType roleType);
    List<Role> findRolesByUserId(Long userId);
    Role remove(Long roleId);
}

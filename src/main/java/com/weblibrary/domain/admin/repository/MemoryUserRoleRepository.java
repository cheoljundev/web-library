package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryUserRoleRepository implements UserRoleRepository {

    private static final Map<Long, Role> store = new HashMap<>();
    public static Long lastId = 0L;

    @Override
    public void save(Role role) {
        store.put(role.getId(), role);
    }

    @Override
    public Role findRoleByUserIdAndRoleType(Long userId, RoleType roleType) {
        for (Role role : store.values()) {
            if (role.getUserId() == userId) {
                if (role.getRoleType() == roleType) {
                    return role;
                }
            }
        }

        return null;
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        List<Role> list = new ArrayList<>();
        for (Role role : store.values()) {
            if (role.getId() == userId) {
                list.add(role);
            }
        }

        return list;
    }

    @Override
    public Role remove(Long roleId) {
        return store.remove(roleId);
    }
}

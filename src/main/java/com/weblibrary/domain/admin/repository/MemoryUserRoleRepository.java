package com.weblibrary.domain.admin.repository;

import com.weblibrary.domain.admin.model.Role;
import com.weblibrary.domain.admin.model.RoleType;

import java.util.*;

public class MemoryUserRoleRepository implements UserRoleRepository {

    private static final Map<Long, Role> store = new HashMap<>();
    public static Long lastId = 0L;

    public static Long incrementLastId() {
        return ++lastId;
    }

    @Override
    public void save(Role role) {
        role.setRoleId(incrementLastId());
        store.put(role.getRoleId(), role);
    }

    @Override
    public Optional<Role> findById(Long roleId) {
        return Optional.ofNullable(store.get(roleId));
    }

    @Override
    public Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType) {

        return findAll().stream().
                filter(role -> role.getUserId().equals(userId))
                .filter(role -> role.getRoleType().equals(roleType))
                .findFirst();
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        List<Role> roles = new ArrayList<>();
        for (Role role : store.values()) {
            if (role.getUserId().equals(userId)) {
                roles.add(role);
            }
        }

        return roles;
    }

    @Override
    public List<Role> findAll() {
        return new ArrayList<>(store.values());
    }

    @Override
    public boolean remove(Long roleId) {
        return store.remove(roleId) != null;
    }
}

package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class MybatisUserRoleRepository implements UserRoleRepository {

    private final UserRoleMapper mapper;

    @Override
    public void save(Role role) {
        mapper.save(role);
    }

    @Override
    public Optional<Role> findById(Long roleId) {
        return mapper.findById(roleId);
    }

    @Override
    public Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType) {
        return mapper.findRoleByUserIdAndRoleType(userId, roleType);
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        return mapper.findRolesByUserId(userId);
    }

    @Override
    public int remove(Long roleId) {
        return mapper.remove(roleId);
    }
}

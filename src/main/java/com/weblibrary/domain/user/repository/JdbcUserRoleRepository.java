package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRoleRepository implements UserRoleRepository {


    private final JdbcTemplate template;

    @Override
    public void save(Role role) {
        String sql = "insert into roles(user_id, role_type) values(?, ?)";
        template.update(sql, role.getUserId(), role.getRoleType().name());
    }

    @Override
    public Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType) {
        String sql = "select * from roles where user_id = ? and role_type = ?";
        try {
            Role role = template.queryForObject(sql, getRoleMapper(), userId, roleType.name());
            return Optional.ofNullable(role);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> findById(Long roleId) {
        String sql = "select * from roles where role_id = ?";
        try {
            Role role = template.queryForObject(sql, getRoleMapper(), roleId);
            return Optional.ofNullable(role);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        String sql = "select * from roles where user_id = ?";
        try {
            return template.query(sql, getRoleMapper(), userId);
        } catch (DataAccessException e) {
            return List.of();
        }
    }

    @Override
    public List<Role> findAll() {
        String sql = "select * from roles";
        try {
            return template.query(sql, getRoleMapper());
        } catch (DataAccessException e) {
            return List.of();
        }
    }

    @Override
    public boolean remove(Long roleId) {
        String sql = "delete from roles where role_id = ?";
        return template.update(sql, roleId) > 0;

    }

    private RowMapper<Role> getRoleMapper() {
        return (rs, rowNum) -> {
            long getRoleId = rs.getLong("role_id");
            long getUserId = rs.getLong("user_id");
            String getRoleType = rs.getString("role_type");
            RoleType roleType = RoleType.valueOf(getRoleType);
            Role role = new Role(getUserId, roleType);
            role.setRoleId(getRoleId);
            return role;
        };
    }
}

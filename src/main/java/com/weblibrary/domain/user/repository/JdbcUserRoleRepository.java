package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

@Repository
public class JdbcUserRoleRepository implements UserRoleRepository {


    private final JdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcUserRoleRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("roles")
                .usingGeneratedKeyColumns("role_id");
    }

    @Override
    public void save(Role role) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(role);
        Number roleId = jdbcInsert.executeAndReturnKey(param);
        role.setRoleId(roleId.longValue());
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

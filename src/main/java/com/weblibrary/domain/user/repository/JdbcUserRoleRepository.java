package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class JdbcUserRoleRepository implements UserRoleRepository {


    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcUserRoleRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("roles")
                .usingGeneratedKeyColumns("role_id");
    }

    @Override
    public void save(Role role) {
        SqlParameterSource param = new MapSqlParameterSource()
                .addValue("user_id", role.getUserId())
                .addValue("role_type", role.getRoleType().name());
        Number roleId = jdbcInsert.executeAndReturnKey(param);
        role.setRoleId(roleId.longValue());
    }

    @Override
    public Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType) {
        String sql = "select * from roles where user_id = :userId and role_type = :roleType";
        try {
            MapSqlParameterSource param = new MapSqlParameterSource()
                    .addValue("userId", userId)
                    .addValue("roleType", roleType.name());
            Role role = template.queryForObject(sql, param, getRoleMapper());
            return Optional.ofNullable(role);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Role> findById(Long roleId) {
        String sql = "select * from roles where role_id = :roleId";
        try {
            Map<String, Long> param = Map.of("roleId", roleId);
            Role role = template.queryForObject(sql, param, getRoleMapper());
            return Optional.ofNullable(role);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        String sql = "select * from roles where user_id = :userId";
        try {
            Map<String, Long> param = Map.of("userId", userId);
            return template.query(sql, param, getRoleMapper());
        } catch (DataAccessException e) {
            return List.of();
        }
    }

    @Override
    public boolean remove(Long roleId) {
        String sql = "delete from roles where role_id = :roleId";
        Map<String, Long> param = Map.of("roleId", roleId);
        return template.update(sql, param) > 0;

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

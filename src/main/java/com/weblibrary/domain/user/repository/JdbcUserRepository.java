package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
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
public class JdbcUserRepository implements UserRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcUserRepository(DataSource dataSource) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("user_id");
    }

    @Override
    public User save(User user) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(user);
        Number userId = jdbcInsert.executeAndReturnKey(param);
        user.setUserId(userId.longValue());
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        String sql = "select * from users where user_id = :userId";
        try {
            Map<String, Long> param = Map.of("userId", id);
            User user = template.queryForObject(sql, param, getUserRowMapper());
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "select * from users where username = :username";
        try {
            Map<String, String> param = Map.of("username", username);
            User user = template.queryForObject(sql, param, getUserRowMapper());
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        String sql = "select * from users order by user_id desc limit :limit offset :offset";
        List<User> users = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("limit", pageable.getPageSize())
                    .addValue("offset", pageable.getOffset());
            users = template.query(sql, param, getUserRowMapper());
        } catch (DataAccessException e) {
            users = List.of();
        }

        // 전체 레코드 수 조회
        String countSql = "select count(*) from users";
        int total = 0;
        try {
            total = template.queryForObject(countSql, Map.of(), Integer.class);
        } catch (DataAccessException e) {
            total = 0;
        }

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public void update(User user) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(user);
        String sql = "update users set " +
                "username = :username, " +
                "password = :password, " +
                "remaining_rents = :remainingRents " +
                "where user_id = :userId";

        template.update(sql, param);
    }

    @Override
    public void remove(Long userId) {
        String sql = "delete from users where user_id=:userId";
        Map<String, Long> param = Map.of("userId", userId);
        template.update(sql, param);
    }


    private static RowMapper<User> getUserRowMapper() {
        return (rs, rowNum) -> {
            long getUserId = rs.getLong("user_id");
            String getUsername = rs.getString("username");
            String getPassword = rs.getString("password");
            int getRemainingRents = rs.getInt("remaining_rents");
            return new User(getUserId, getUsername, getPassword, getRemainingRents);
        };
    }
}

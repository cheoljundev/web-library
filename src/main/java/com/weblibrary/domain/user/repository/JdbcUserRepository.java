package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public Optional<User> findById(Long userId) {
        String sql = "select * from users where user_id = :userId";
        try {
            Map<String, Long> param = Map.of("userId", userId);
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
    public List<User> findAll(Number limit, Number offset) {
        String sql = "select * from users order by user_id desc limit :limit offset :offset";
        List<User> users = null;
        try {
            SqlParameterSource param = new MapSqlParameterSource()
                    .addValue("limit", limit)
                    .addValue("offset", offset);
            users = template.query(sql, param, getUserRowMapper());
        } catch (DataAccessException e) {
            users = List.of();
        }

        return users;
    }

    @Override
    public int countAll() {
        String sql = "select count(*) from users";
        return template.queryForObject(sql, Map.of(), Integer.class);
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

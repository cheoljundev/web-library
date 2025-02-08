package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.exception.NotFoundUserException;
import com.weblibrary.domain.user.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate template;
    private final SimpleJdbcInsert jdbcInsert;

    public JdbcUserRepository(DataSource dataSource) {
        this.template = new JdbcTemplate(dataSource);
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
        String sql = "select * from users where user_id = ?";
        try {
            User user = template.queryForObject(sql, getUserRowMapper(), id);
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "select * from users where username = ?";
        try {
            User user = template.queryForObject(sql, getUserRowMapper(), username);
            return Optional.ofNullable(user);
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        String sql = "select * from users order by user_id desc limit ? offset ?";
        List<User> users = null;
        try {
            users = template.query(sql, getUserRowMapper(), pageable.getPageSize(), pageable.getOffset());
        } catch (DataAccessException e) {
            users = List.of();
        }

        // 전체 레코드 수 조회
        String countSql = "select count(*) from users";
        int total = 0;
        try {
            total = template.queryForObject(countSql, Integer.class);
        } catch (DataAccessException e) {
            total = 0;
        }

        return new PageImpl<>(users, pageable, total);
    }

    @Override
    public User update(User user) {
        User oldUser = findById(user.getUserId())
                .orElseThrow(NotFoundUserException::new);

        String sql = "update users set " +
                "username = ?, " +
                "password = ?, " +
                "remaining_rents = ? " +
                "where user_id = ?";

        template.update(sql, user.getUsername(), user.getPassword(), user.getRemainingRents(), user.getUserId());

        return oldUser;
    }

    @Override
    public Optional<User> remove(Long userId) {
        String sql = "delete from users where user_id=?";

        User user = findById(userId)
                .orElseThrow(NotFoundUserException::new);

        template.update(sql, userId);
        return Optional.of(user);
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

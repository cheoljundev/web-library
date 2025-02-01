package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.List;
import java.util.Optional;

import static com.weblibrary.web.connection.DBConnectionUtil.close;
import static com.weblibrary.web.connection.DBConnectionUtil.getConnection;

@Repository
public class JdbcUserRepository implements UserRepository {

    @Override
    public User save(User user) {
        String sql = "insert into users(username, password) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) { // 커서를 첫 번째 행으로 이동
                    Long userId = rs.getLong(1);
                    user.setUserId(userId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.empty();
    }

    @Override
    public List<User> findAll() {
        return List.of();
    }

    @Override
    public Optional<User> remove(Long userId) {
        return Optional.empty();
    }

    @Override
    public void clearAll() {

    }
}

package com.weblibrary.domain.user.repository;

import com.weblibrary.domain.user.model.User;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
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
        String sql = "select * from users where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, id);
            rs = pstmt.executeQuery();

            User user = null;

            if (rs.next()) {
                long getUserId = rs.getLong("user_id");
                String getUsername = rs.getString("username");
                String getPassword = rs.getString("password");
                int getRemainingRents = rs.getInt("remaining_rents");
                user = new User(getUserId, getUsername, getPassword, getRemainingRents);
            }

            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        String sql = "select * from users where username = ?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();

            User user = null;

            if (rs.next()) {
                long getUserId = rs.getLong("user_id");
                String getUsername = rs.getString("username");
                String getPassword = rs.getString("password");
                int getRemainingRents = rs.getInt("remaining_rents");
                user = new User(getUserId, getUsername, getPassword, getRemainingRents);
            }

            return Optional.ofNullable(user);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public List<User> findAll() {
        String sql = "select * from users";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<User> users = new ArrayList<>();

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long getUserId = rs.getLong("user_id");
                String getUsername = rs.getString("username");
                String getPassword = rs.getString("password");
                int getRemainingRents = rs.getInt("remaining_rents");
                users.add(new User(getUserId, getUsername, getPassword, getRemainingRents));
            }

            return users;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }

    }

    @Override
    public Optional<User> remove(Long userId) {
        String sql = "delete from users where user_id=?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;


        try {
            User removed = findById(userId).orElse(null);

            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.executeUpdate();

            return Optional.ofNullable(removed);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            close(con, pstmt, rs);
        }
    }

    @Override
    public void clearAll() {

    }
}

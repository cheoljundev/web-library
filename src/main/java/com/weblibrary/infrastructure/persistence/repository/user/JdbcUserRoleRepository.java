package com.weblibrary.infrastructure.persistence.repository.user;

import com.weblibrary.domain.user.model.Role;
import com.weblibrary.domain.user.model.RoleType;
import com.weblibrary.domain.user.repository.UserRoleRepository;
import com.weblibrary.web.connection.DBConnectionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class JdbcUserRoleRepository implements UserRoleRepository {

    private final DBConnectionUtil dbConnectionUtil;


    @Override
    public void save(Role role) {
        String sql = "insert into roles(user_id, role_type) values(?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setLong(1, role.getUserId());
            pstmt.setString(2, role.getRoleType().name());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) { // 커서를 첫 번째 행으로 이동
                    Long roleId = rs.getLong(1);
                    role.setRoleId(roleId);
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Role> findRoleByUserIdAndRoleType(Long userId, RoleType roleType) {
        String sql = "select * from roles where user_id = ? and role_type = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        Role role = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, userId);
            pstmt.setString(2, roleType.name());
            rs = pstmt.executeQuery();

            if (rs.next()) { // 커서를 첫 번째 행으로 이동
                long getRoleId = rs.getLong(1);
                role = new Role(userId, roleType);
                role.setRoleId(getRoleId);
            }

            return Optional.ofNullable(role);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }
    }

    @Override
    public Optional<Role> findById(Long roleId) {
        String sql = "select * from roles where role_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, roleId);
            rs = pstmt.executeQuery();

            Role role = null;

            if (rs.next()) {
                long getUserId = rs.getLong(2);
                RoleType roleType = RoleType.valueOf(rs.getString(3));
                role = new Role(getUserId, roleType);
                role.setRoleId(roleId);
            }

            return Optional.ofNullable(role);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public List<Role> findRolesByUserId(Long userId) {
        String sql = "select * from roles where user_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Role> roles = new ArrayList<>();

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, userId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long getRoleId = rs.getLong(1);
                String  getRoleType = rs.getString(3);
                RoleType roleType = RoleType.valueOf(getRoleType);
                Role role = new Role(userId, roleType);
                role.setRoleId(getRoleId);
                roles.add(role);
            }

            return roles;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Role> findAll() {
        String sql = "select * from roles";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Role> roles = new ArrayList<>();

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                long getRoleId = rs.getLong(1);
                long getUserId = rs.getLong(2);
                String  getRoleType = rs.getString(3);
                RoleType roleType = RoleType.valueOf(getRoleType);
                Role role = new Role(getUserId, roleType);
                role.setRoleId(getRoleId);
                roles.add(role);
            }

            return roles;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean remove(Long roleId) {
        String sql = "delete from roles where role_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = dbConnectionUtil.getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setLong(1, roleId);
            int executed = pstmt.executeUpdate();
            return executed > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dbConnectionUtil.close(con, pstmt, rs);
        }

    }
}

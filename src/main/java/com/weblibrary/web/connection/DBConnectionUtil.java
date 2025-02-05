package com.weblibrary.web.connection;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.weblibrary.web.connection.ConnectionConst.*;

@Slf4j
@Component
public class DBConnectionUtil {

    private final DataSource dataSource;

    public DBConnectionUtil() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(URL);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        dataSource.setPoolName("WebLibraryPool");
        this.dataSource = dataSource;
    }

    public Connection getConnection() throws SQLException {
        Connection con = dataSource.getConnection();
        log.debug("get connection={}, class={}", con, con.getClass());
        return con;
    }

    public void close(Connection con, Statement stmt, ResultSet rs) {

        JdbcUtils.closeResultSet(rs);
        JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);

    }
}

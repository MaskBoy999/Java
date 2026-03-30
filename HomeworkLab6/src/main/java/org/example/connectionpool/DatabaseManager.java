package org.example.connectionpool;


import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseManager {
    private static final HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/movie_db");
        config.setUsername("postgres");
        config.setPassword("STUDENT");

        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);
    }

    private DatabaseManager() {}

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}
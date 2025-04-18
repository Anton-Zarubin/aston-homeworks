package com.example.newsservice.util;

import com.example.newsservice.config.DBConfig;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class DBConnection {

    private static final DataSource dataSource = DBConfig.getDataSource();

    private DBConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }
}

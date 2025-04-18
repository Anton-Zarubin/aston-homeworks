package com.example.newsservice.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.NoArgsConstructor;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

@NoArgsConstructor
public class DBConfig {

    private static final Properties properties = new Properties();

    static {
        try {
            properties.load(DBConfig.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DataSource getDataSource() {
        HikariConfig config = new HikariConfig("db.properties");
        return new HikariDataSource(config);
    }
}

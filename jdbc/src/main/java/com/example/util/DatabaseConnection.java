package com.example.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static {
        Properties env = new Properties();
        try (InputStream input = new FileInputStream(".env")) {
            env.load(input);
        } catch (IOException e) {
            System.out.println("Error loading .env: " + e.getMessage());
        }
        URL = String.format("jdbc:postgresql://%s:%s/%s",
                env.getProperty("DB_HOST"),
                env.getProperty("DB_PORT"),
                env.getProperty("DB_NAME"));
        USER = env.getProperty("DB_USER");
        PASSWORD = env.getProperty("DB_PASSWORD");
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

}

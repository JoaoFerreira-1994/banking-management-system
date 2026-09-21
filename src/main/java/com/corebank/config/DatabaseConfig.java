package com.corebank.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {

    private static final String DATABASE_PATH =
            System.getProperty("corebank.db.path", "corebank.db");

    private static final String URL =
            "jdbc:sqlite:" + DATABASE_PATH;

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println(
                "SQLite JDBC driver not found: " + e.getMessage()
            );
        }
    }

    public static Connection connect() throws SQLException {
    return DriverManager.getConnection(URL);
}
}
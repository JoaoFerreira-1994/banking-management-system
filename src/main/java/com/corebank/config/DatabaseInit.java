package com.corebank.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {
    public static void createTable(){
        String sqlClients = """
            CREATE TABLE IF NOT EXISTS clients (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                nif TEXT NOT NULL UNIQUE,
                email TEXT,
                phoneNumber TEXT,
                Status TEXT
            )
        """;
        
        try (
            Connection conn = DatabaseConfig.connect();
            Statement stmt = conn.createStatement()
        ){
            stmt.execute(sqlClients);
            System.out.print("Table created successfully!");
        }
        
        catch (SQLException e) {
            System.out.print("Error creating table:  " + e.getMessage());
        }
    }


    // public static void main(String[] args) {
    //     createTable();
    // }
}

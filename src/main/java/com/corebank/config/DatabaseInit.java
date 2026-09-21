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

        String sqlAccounts = """
            CREATE TABLE IF NOT EXISTS accounts (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                iban TEXT NOT NULL UNIQUE,
                accountType TEXT NOT NULL,
                balance REAL NOT NULL DEFAULT 0,
                status TEXT NOT NULL,
                clientId INTEGER NOT NULL,
                FOREIGN KEY (clientId) REFERENCES clients(id)
            )
            """;
        
        String sqlTransactions = """
            CREATE TABLE IF NOT EXISTS transactions (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                type TEXT NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                accountId INTEGER NOT NULL,
                FOREIGN KEY (accountId) REFERENCES accounts(id)
            )
            """;

        String sqlTransfers = """
            CREATE TABLE IF NOT EXISTS transfers (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                sourceAccountId INTEGER NOT NULL,
                destinationAccountId INTEGER NOT NULL,
                amount REAL NOT NULL,
                date TEXT NOT NULL,
                status TEXT NOT NULL,
                FOREIGN KEY (sourceAccountId) REFERENCES accounts(id),
                FOREIGN KEY (destinationAccountId) REFERENCES accounts(id)
            )
            """;
        
        try (
            Connection conn = DatabaseConfig.connect();
            Statement stmt = conn.createStatement()
        ){
            stmt.execute(sqlClients);
            stmt.execute(sqlAccounts);
            stmt.execute(sqlTransactions);
            stmt.execute(sqlTransfers);

            System.out.print("Tables created successfully!");
        }
        
        catch (SQLException e) {
            System.out.print("Error creating table:  " + e.getMessage());
        }
    }

}

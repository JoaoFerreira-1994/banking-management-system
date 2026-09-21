package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Transaction;

public class TransactionRepository {

// ------------------------------ CREATE ------------------------------

    public void createTransaction(Transaction transaction) {

        String sql = """
            INSERT INTO transactions
            (type, amount, date, accountId)
            VALUES (?, ?, ?, ?)
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, transaction.getType());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setString(3, transaction.getDate().toString());
            stmt.setInt(4, transaction.getAccountId());

            stmt.executeUpdate();

            try (
                PreparedStatement idStmt =
                    conn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rs = idStmt.executeQuery()
            ) {

                if (rs.next()) {
                    transaction.setId(rs.getInt(1));
                }
            }

            System.out.println("Transaction created successfully!");

        } catch (SQLException e) {
            System.out.println(
                "Error creating transaction: " + e.getMessage()
            );
        }
    }


    // ------------------------------ CREATE WITH CONNECTION ------------------------------

    public void createTransaction(
            Connection conn,
            Transaction transaction) throws SQLException {

        String sql = """
            INSERT INTO transactions
            (type, amount, date, accountId)
            VALUES (?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, transaction.getType());
            stmt.setDouble(2, transaction.getAmount());
            stmt.setString(3, transaction.getDate().toString());
            stmt.setInt(4, transaction.getAccountId());

            stmt.executeUpdate();
        }

        try (
            PreparedStatement idStmt =
                conn.prepareStatement("SELECT last_insert_rowid()");
            ResultSet rs = idStmt.executeQuery()
        ) {

            if (rs.next()) {
                transaction.setId(rs.getInt(1));
            }
        }
    }

    // ------------------------------ LIST ------------------------------

    public List<Transaction> listTransactions() {

        List<Transaction> transactions = new ArrayList<>();

        String sql = "SELECT * FROM transactions";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Transaction transaction = new Transaction(
                    rs.getInt("id"),
                    rs.getString("type"),
                    rs.getDouble("amount"),
                    LocalDateTime.parse(rs.getString("date")),
                    rs.getInt("accountId")
                );

                transactions.add(transaction);
            }

        } catch (SQLException e) {
            System.out.println(
                "Error listing transactions: " + e.getMessage()
            );
        }

        return transactions;
    }

    // ------------------------------ FIND BY ID ------------------------------

    public Transaction findTransactionID(int id) {

        String sql = "SELECT * FROM transactions WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getInt("accountId")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                "Error finding transaction: " + e.getMessage()
            );
        }

        return null;
    }

    // ------------------------------ LIST BY ACCOUNT ------------------------------

    public List<Transaction> listTransactionsByAccount(int accountId) {

        List<Transaction> transactions = new ArrayList<>();

        String sql = """
            SELECT * FROM transactions
            WHERE accountId = ?
            ORDER BY date DESC
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getInt("accountId")
                    );

                    transactions.add(transaction);
                }
            }

        } catch (SQLException e) {
            System.out.println(
                "Error listing account transactions: " + e.getMessage()
            );
        }

        return transactions;
    }
}
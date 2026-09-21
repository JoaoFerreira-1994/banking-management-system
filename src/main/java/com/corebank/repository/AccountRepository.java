package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Account;


public class AccountRepository {

    // ------------------------------ CREATE ------------------------------

    public void createAccount(Account account) {

        String sql = """
            INSERT INTO accounts
            (iban, accountType, balance, status, clientId)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, account.getIban());
            stmt.setString(2, account.getAccountType());
            stmt.setDouble(3, account.getBalance());
            stmt.setString(4, account.getStatus());
            stmt.setInt(5, account.getClientId());

            stmt.executeUpdate();

            try (
                PreparedStatement idStmt =
                    conn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rs = idStmt.executeQuery()
            ) {

                if (rs.next()) {
                    account.setId(rs.getInt(1));
                }
            }

            System.out.println("Account created successfully!");

        } catch (SQLException e) {
            System.out.println(
                "Error creating account: " + e.getMessage()
            );
        }
    }

    // ------------------------------ LIST ------------------------------

    public List<Account> listAccounts() {

        List<Account> accounts = new ArrayList<>();

        String sql = "SELECT * FROM accounts";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Account account = new Account(
                    rs.getInt("id"),
                    rs.getString("iban"),
                    rs.getString("accountType"),
                    rs.getDouble("balance"),
                    rs.getString("status"),
                    rs.getInt("clientId")
                );

                accounts.add(account);
            }

        } catch (SQLException e) {
            System.out.println(
                "Error listing accounts: " + e.getMessage()
            );
        }

        return accounts;
    }

    // ------------------------------ FIND BY ID ------------------------------

    public Account findAccountID(int id) {

        String sql = "SELECT * FROM accounts WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Account(
                        rs.getInt("id"),
                        rs.getString("iban"),
                        rs.getString("accountType"),
                        rs.getDouble("balance"),
                        rs.getString("status"),
                        rs.getInt("clientId")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                "Error finding account: " + e.getMessage()
            );
        }

        return null;
    }

    // ------------------------------ UPDATE ------------------------------

    public void updateAccount(Account account) {

        String sql = """
            UPDATE accounts
            SET iban = ?, accountType = ?, status = ?, clientId = ?
            WHERE id = ?
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, account.getIban());
            stmt.setString(2, account.getAccountType());
            stmt.setString(3, account.getStatus());
            stmt.setInt(4, account.getClientId());
            stmt.setInt(5, account.getId());

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Account updated successfully!");
            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException e) {
            System.out.println(
                "Error updating account: " + e.getMessage()
            );
        }
    }

    // ------------------------------ DELETE ------------------------------

    public void deleteAccount(int id) {

        String sql = "DELETE FROM accounts WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Account deleted successfully!");
            } else {
                System.out.println("Account not found.");
            }

        } catch (SQLException e) {
            System.out.println(
                "Error deleting account: " + e.getMessage()
            );
        }
    }

    // ------------------------------ UPDATE BALANCE ------------------------------

    public boolean updateBalance(int accountId, double newBalance) {

        String sql = """
            UPDATE accounts
            SET balance = ?
            WHERE id = ?
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setDouble(1, newBalance);
            stmt.setInt(2, accountId);

            int rows = stmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Account balance updated successfully!");
                return true;
            }

            System.out.println("Account not found.");
            return false;

        } catch (SQLException e) {
            System.out.println(
                "Error updating account balance: " + e.getMessage()
            );
            return false;
        }
    }

    // ------------------------------ UPDATE WITH CONNECTION ------------------------------
    
    public boolean updateBalance(
        Connection conn,
        int accountId,
        double newBalance) throws SQLException {

    String sql = """
        UPDATE accounts
        SET balance = ?
        WHERE id = ?
        """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setDouble(1, newBalance);
        stmt.setInt(2, accountId);

        int rows = stmt.executeUpdate();

        return rows > 0;
    }
}














}
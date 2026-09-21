package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Transfer;

public class TransferRepository {

    // ------------------------------ CREATE ------------------------------

    public void createTransfer(Transfer transfer) {

        String sql = """
            INSERT INTO transfers
            (sourceAccountId, destinationAccountId, amount, date, status)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, transfer.getSourceAccountId());
            stmt.setInt(2, transfer.getDestinationAccountId());
            stmt.setDouble(3, transfer.getAmount());
            stmt.setString(4, transfer.getDate().toString());
            stmt.setString(5, transfer.getStatus());

            stmt.executeUpdate();

            try (
                PreparedStatement idStmt =
                    conn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rs = idStmt.executeQuery()
            ) {

                if (rs.next()) {
                    transfer.setId(rs.getInt(1));
                }
            }

            System.out.println("Transfer created successfully!");

        } catch (SQLException e) {
            System.out.println(
                "Error creating transfer: " + e.getMessage()
            );
        }
    }

    // ------------------------------ CREATE WITH CONNECTION ------------------------------
    
    public void createTransfer(
        Connection conn,
        Transfer transfer) throws SQLException {

    String sql = """
        INSERT INTO transfers
        (sourceAccountId, destinationAccountId, amount, date, status)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, transfer.getSourceAccountId());
        stmt.setInt(2, transfer.getDestinationAccountId());
        stmt.setDouble(3, transfer.getAmount());
        stmt.setString(4, transfer.getDate().toString());
        stmt.setString(5, transfer.getStatus());

        stmt.executeUpdate();
    }

    try (
        PreparedStatement idStmt =
            conn.prepareStatement("SELECT last_insert_rowid()");
        ResultSet rs = idStmt.executeQuery()
    ) {

        if (rs.next()) {
            transfer.setId(rs.getInt(1));
        }
    }
}

    // ------------------------------ LIST ------------------------------

    public List<Transfer> listTransfers() {

        List<Transfer> transfers = new ArrayList<>();

        String sql = "SELECT * FROM transfers ORDER BY date DESC";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Transfer transfer = new Transfer(
                    rs.getInt("id"),
                    rs.getInt("sourceAccountId"),
                    rs.getInt("destinationAccountId"),
                    rs.getDouble("amount"),
                    LocalDateTime.parse(rs.getString("date")),
                    rs.getString("status")
                );

                transfers.add(transfer);
            }

        } catch (SQLException e) {
            System.out.println(
                "Error listing transfers: " + e.getMessage()
            );
        }

        return transfers;
    }

    // ------------------------------ FIND BY ID ------------------------------

    public Transfer findTransferID(int id) {

        String sql = "SELECT * FROM transfers WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Transfer(
                        rs.getInt("id"),
                        rs.getInt("sourceAccountId"),
                        rs.getInt("destinationAccountId"),
                        rs.getDouble("amount"),
                        LocalDateTime.parse(rs.getString("date")),
                        rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println(
                "Error finding transfer: " + e.getMessage()
            );
        }

        return null;
    }
}
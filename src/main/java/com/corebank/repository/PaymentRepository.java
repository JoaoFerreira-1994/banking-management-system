package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Payment;

public class PaymentRepository {

    private static final DateTimeFormatter DATE_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // ------------------------------ CREATE WITH CONNECTION ------------------------------

    public void createPayment(
            Connection conn,
            Payment payment) throws SQLException {

        String sql = """
            INSERT INTO payments
            (accountId, entity, reference, amount, date, status)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, payment.getAccountId());
            stmt.setString(2, payment.getEntity());
            stmt.setString(3, payment.getReference());
            stmt.setDouble(4, payment.getAmount());
            stmt.setString(5, payment.getDate().format(DATE_FORMATTER));
            stmt.setString(6, payment.getStatus());

            stmt.executeUpdate();
        }

        try (
            PreparedStatement idStmt =
                conn.prepareStatement("SELECT last_insert_rowid()");
            ResultSet rs = idStmt.executeQuery()
        ) {
            if (rs.next()) {
                payment.setId(rs.getInt(1));
            }
        }
    }

    // ------------------------------ LIST ------------------------------

    public List<Payment> listPayments() {

        List<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM payments ORDER BY date DESC";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Payment payment = new Payment(
                    rs.getInt("id"),
                    rs.getInt("accountId"),
                    rs.getString("entity"),
                    rs.getString("reference"),
                    rs.getDouble("amount"),
                    parseDate(rs.getString("date")),
                    rs.getString("status")
                );

                payments.add(payment);
            }

        } catch (SQLException e) {
            System.out.println("Error listing payments: " + e.getMessage());
        }

        return payments;
    }


    // ------------------------------ FIND BY ID ------------------------------

    public Payment findPaymentID(int id) {

        String sql = "SELECT * FROM payments WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Payment(
                        rs.getInt("id"),
                        rs.getInt("accountId"),
                        rs.getString("entity"),
                        rs.getString("reference"),
                        rs.getDouble("amount"),
                        parseDate(rs.getString("date")),
                        rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding payment: " + e.getMessage());
        }

        return null;
    }

    // ------------------------------ PARSE DATE ------------------------------
    private LocalDateTime parseDate(String date) {

        try {
            return LocalDateTime.parse(date, DATE_FORMATTER);
        } catch (Exception e) {
            return LocalDateTime.parse(date);
        }
    }
}
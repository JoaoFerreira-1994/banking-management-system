package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.User;

public class UserRepository {

    // ------------------------------ CREATE ------------------------------

    public void createUser(User user) {

        String sql = """
            INSERT INTO users
            (username, password, role, clientId, employeeId)
            VALUES (?, ?, ?, ?, ?)
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());

            if (user.getClientId() != null) {
                stmt.setInt(4, user.getClientId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            if (user.getEmployeeId() != null) {
                stmt.setInt(5, user.getEmployeeId());
            } else {
                stmt.setNull(5, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            try (
                PreparedStatement idStmt =
                    conn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rs = idStmt.executeQuery()
            ) {
                if (rs.next()) {
                    user.setId(rs.getInt(1));
                }
            }

            System.out.println("User created successfully!");

        } catch (SQLException e) {
            System.out.println("Error creating user: " + e.getMessage());
        }
    }


    // ------------------------------ FIND BY USERNAME ------------------------------

    public User findByUsername(String username) {

        String sql = "SELECT * FROM users WHERE username = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    Integer clientId =
                        rs.getObject("clientId") != null
                            ? rs.getInt("clientId")
                            : null;

                    Integer employeeId =
                        rs.getObject("employeeId") != null
                            ? rs.getInt("employeeId")
                            : null;

                    return new User(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        clientId,
                        employeeId
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding user: " + e.getMessage());
        }

        return null;
    }
}
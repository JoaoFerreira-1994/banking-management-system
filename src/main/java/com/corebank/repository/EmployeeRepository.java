package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Employee;

public class EmployeeRepository {

    // ------------------------------ CREATE ------------------------------

    public boolean createEmployee(Employee employee) {

        String sql = """
            INSERT INTO employees
            (name, email, phoneNumber, status)
            VALUES (?, ?, ?, ?)
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhoneNumber());
            stmt.setString(4, employee.getStatus());

            int rows = stmt.executeUpdate();

            if (rows == 0) {
                return false;
            }

            try (
                PreparedStatement idStmt =
                    conn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rs = idStmt.executeQuery()
            ) {
                if (rs.next()) {
                    employee.setId(rs.getInt(1));
                }
            }

            return employee.getId() > 0;

        } catch (SQLException e) {

            System.out.println(
                "Error creating employee: " + e.getMessage()
            );

            return false;
        }
    }


    // ------------------------------ LIST ------------------------------

    public List<Employee> listEmployees() {

        List<Employee> employees = new ArrayList<>();

        String sql = "SELECT * FROM employees";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                Employee employee = new Employee(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"),
                    rs.getString("status")
                );

                employees.add(employee);
            }

        } catch (SQLException e) {
            System.out.println("Error listing employees: " + e.getMessage());
        }

        return employees;
    }


    // ------------------------------ FIND BY ID ------------------------------

    public Employee findEmployeeID(int id) {

        String sql = "SELECT * FROM employees WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {

                    return new Employee(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("phoneNumber"),
                        rs.getString("status")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error finding employee: " + e.getMessage());
        }

        return null;
    }


    // ------------------------------ UPDATE ------------------------------

    public boolean updateEmployee(Employee employee) {

        String sql = """
            UPDATE employees
            SET name = ?,
                email = ?,
                phoneNumber = ?,
                status = ?
            WHERE id = ?
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, employee.getName());
            stmt.setString(2, employee.getEmail());
            stmt.setString(3, employee.getPhoneNumber());
            stmt.setString(4, employee.getStatus());
            stmt.setInt(5, employee.getId());

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error updating employee: " + e.getMessage());
            return false;
        }
    }


    // ------------------------------ DELETE ------------------------------

    public boolean deleteEmployee(int id) {

        String sql = "DELETE FROM employees WHERE id = ?";

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setInt(1, id);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Error deleting employee: " + e.getMessage());
            return false;
        }
    }
}
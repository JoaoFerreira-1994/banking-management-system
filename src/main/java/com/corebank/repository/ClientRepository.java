package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Client;



public class ClientRepository {

// ---------------------------------- CREATE -----------------------------------------
    public static boolean createClient(Client client) {

        String sql = """
            INSERT INTO clients (name, nif, email, phoneNumber, status)
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, client.getName());
            stmt.setString(2, client.getNif());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getStatus());

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected == 0) {
                return false;
            }

            try (
                PreparedStatement idStmt =
                    conn.prepareStatement("SELECT last_insert_rowid()");
                ResultSet rs = idStmt.executeQuery()
            ) {
                if (rs.next()) {
                    client.setId(rs.getInt(1));
                }
            }

            System.out.println("Client created successfully!");
            return true;

        } catch (SQLException e) {

            System.out.println(
                "Error creating client: " + e.getMessage()
            );

            return false;
        }
    }


// ---------------------------------- LIST -----------------------------------------

    public List<Client> listClients(){

        List<Client> clients = new ArrayList<>();

        String sql = "SELECT * FROM clients";

        try(
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
        ) {
            while (rs.next()){
                Client client = new Client(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("nif"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"),
                    rs.getString("status")
                );

                clients.add(client);
            }
            
        } catch ( SQLException  e) {
            System.out.print("Error listing clients: " + e.getMessage());
        }

        return clients;
    }

// ---------------------------------- Find by ID -----------------------------------------

    public Client findClientID(int id){

        String sql = "SELECT * FROM clients WHERE id = ?";
             
        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Client client = new Client(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("nif"),
                    rs.getString("email"),
                    rs.getString("phoneNumber"),
                    rs.getString("status")
                );
                return client;
            }
            return null;
        }catch (SQLException e) {
        System.out.println("Error finding client: " + e.getMessage());
        return null;
        }
    }
    
// ---------------------------------- Update -----------------------------------------

    public void updateClient(Client client){

        String sql = """
                Update clients
                SET name = ?, nif = ?, email = ?, phoneNumber = ?, status = ?
                WHERE id = ?
                """;
             
        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getNif());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getStatus());
            stmt.setInt(6, client.getId());
        
            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Client updated successfully!");
            } 
            else {
                System.out.println("Client not found.");
            }
        }

        catch (SQLException e) {System.out.println("Error updating client: " + e.getMessage());}
    }


    // ------------------------- UPDATE CLIENT STATUS -------------------------

    public boolean updateClientStatus(int id, String status) {

        String sql = """
            UPDATE clients
            SET status = ?
            WHERE id = ?
            """;

        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            int rowsAffected = stmt.executeUpdate();

            return rowsAffected > 0;

        } catch (SQLException e) {

            System.out.println("Error updating client status: " + e.getMessage());
            return false;
        }
    }


// ---------------------------------- Delete -----------------------------------------

    public void deleteClient(int id) {

        String sql = "DELETE FROM clients WHERE id = ?";

         try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)){
            
            stmt.setInt(1, id);

            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {System.out.println("Client deleted successfully!");} 
            else {System.out.println("Client not found.");}
            }

        catch (SQLException e) {System.out.println("Error deleting client: " + e.getMessage());}

    }

}

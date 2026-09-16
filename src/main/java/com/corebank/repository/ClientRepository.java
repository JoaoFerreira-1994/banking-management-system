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

    public static void createClient(Client client){
        String sql = """
            INSERT INTO clients (name, nif, email, phoneNumber, status)
            values(?, ?, ?, ?, ?)
        """;
    
        try (
            Connection conn = DatabaseConfig.connect();
            PreparedStatement stmt = conn.prepareStatement(sql)
        ){
            stmt.setString(1, client.getName());
            stmt.setString(2, client.getNif());
            stmt.setString(3, client.getEmail());
            stmt.setString(4, client.getPhoneNumber());
            stmt.setString(5, client.getStatus());

            stmt.executeUpdate();

            System.out.print("Client created successfully!");
        }
        
        catch (SQLException e) {
            System.out.print("Error creating client: " + e.getMessage());
        }
    }

    // ---------------------------------- CÓDIGO DE TESTE -----------------------------------------

    // public static void main(String[] args) {

    //     Client client = new Client(
    //         0,
    //         "John p silva",
    //         "623456900",
    //         "john@email.com",
    //         "912345678",
    //         "ACTIVE"
    //     );

    //     ClientRepository repository = new ClientRepository();

    //     repository.createClient(client);
    // }

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

    // ---------------------------------- CÓDIGO DE TESTE -----------------------------------------

    // public static void main(String[] args) {

    //     ClientRepository repository = new ClientRepository();

    //     List<Client> clients = repository.listClients();

    //     System.out.println("Number of clients: " + clients.size());
        
    //     for (Client client : clients) {

    //         System.out.println(
    //             client.getId() + " | " +
    //             client.getName() + " | " +
    //             client.getNif() + " | " +
    //             client.getEmail() + " | " +
    //             client.getPhoneNumber() + " | " +
    //             client.getStatus()
    //         );
    //     }
    // }

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

    // ---------------------------------- CÓDIGO DE TESTE -----------------------------------------

//     public static void main(String[] args) {

//     ClientRepository repository = new ClientRepository();

//     Client client = repository.findClientID(999);

//     if (client != null) {
//         System.out.println(
//             client.getId() + " | " +
//             client.getName() + " | " +
//             client.getNif() + " | " +
//             client.getEmail() + " | " +
//             client.getPhoneNumber() + " | " +
//             client.getStatus()
//         );
//     } else {
//         System.out.println("Client not found.");
//     }
// }
    
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

    // ---------------------------------- CÓDIGO DE TESTE -----------------------------------------

//     public static void main(String[] args) {

//     ClientRepository repository = new ClientRepository();

//     Client client = repository.findClientID(1);

//     if (client != null) {

//         client.setName("John Updated");
//         client.setEmail("john.updated@email.com");
//         client.setPhoneNumber("919999999");

//         repository.updateClient(client);

//         Client updatedClient = repository.findClientID(1);

//         System.out.println(
//             updatedClient.getId() + " | " +
//             updatedClient.getName() + " | " +
//             updatedClient.getNif() + " | " +
//             updatedClient.getEmail() + " | " +
//             updatedClient.getPhoneNumber() + " | " +
//             updatedClient.getStatus()
//         );

//     } else {
//         System.out.println("Client not found.");
//     }
// }

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

    // ---------------------------------- CÓDIGO DE TESTE -----------------------------------------

    // public static void main(String[] args) {

    //     ClientRepository repository = new ClientRepository();

    //     repository.deleteClient(2);

    //     Client client = repository.findClientID(2);

    //     if (client == null) {
    //         System.out.println("Confirmed: client no longer exists.");
    //     } else {
    //         System.out.println("Client still exists.");
    //     }
    // }







}

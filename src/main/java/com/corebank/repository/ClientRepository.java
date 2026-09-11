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
                stmt.setString(4, client.getStatus());

                stmt.executeUpdate();

                System.out.print("Client created successfully!");
            }
            
            catch (SQLException e) {
                System.out.print("Error creating client: " + e.getMessage());
            }
        }

        // public static void main(String[] args) {

        //     Client client = new Client(
        //         0,
        //         "John Smith",
        //         "123456789",
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

























}

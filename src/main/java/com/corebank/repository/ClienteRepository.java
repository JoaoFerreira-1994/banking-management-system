package com.corebank.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Cliente;


public class ClienteRepository {
        public static void criarCliente(Cliente cliente){
            String sql = """
                INSERT INTO clientes (nome, nif, email, telefone)
                values(?, ?, ?, ?)
            """;
        
            try (
                Connection conn = DatabaseConfig.conectar();
                PreparedStatement stmt = conn.prepareStatement(sql)
            ){
                stmt.setString(1, cliente.getNome());
                stmt.setString(2, cliente.getNif());
                stmt.setString(3, cliente.getEmail());
                stmt.setString(4, cliente.getTelefone());

                stmt.executeUpdate();

                System.out.print("Cliente criada com sucesso!");
            }
            
            catch (SQLException e) {
                System.out.print("Erro ao criar cliente: " + e.getMessage());
            }
        }

        public static void main(String[] args) {

        Cliente cliente = new Cliente(
            0,
            "João Ferreira",
            "123456789",
            "joao@email.pt",
            "912345678"
        );

        ClienteRepository repository = new ClienteRepository();

        repository.criarCliente(cliente);
    }
}

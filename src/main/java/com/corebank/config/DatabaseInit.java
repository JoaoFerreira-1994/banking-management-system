package com.corebank.config;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInit {
    public static void criarTabelas(){
        String sqlClientes = """
            CREATE TABLE IF NOT EXISTS clientes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nome TEXT NOT NULL,
                nif TEXT NOT NULL UNIQUE,
                email TEXT,
                telefone TEXT
            )
        """;
        
        try (
            Connection conn = DatabaseConfig.conectar();
            Statement stmt = conn.createStatement()
        ){
            stmt.execute(sqlClientes);
            System.out.print("Tabela criada com sucesso!");
        }
        
        catch (SQLException e) {
            System.out.print("Erro ao criar tabela de clientes: " + e.getMessage());
        }
    }
}

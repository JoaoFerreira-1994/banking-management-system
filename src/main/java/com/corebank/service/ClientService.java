package com.corebank.service;

import java.util.List;

import com.corebank.config.DatabaseInit;
import com.corebank.model.Client;
import com.corebank.repository.ClientRepository;

import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

// ------------------------------------------ CORE Rules -------------------------------------------

@ApplicationScoped
public class ClientService {

    @PostConstruct
    public void init() {
        DatabaseInit.createTable();
    }

    // ------------------------------- Validate Client Info ---------------------------------

    private boolean validateClient(Client client) {

        if (client == null) {
            System.out.println("Client data is required.");
            return false;
        }

        // ---------------------------- Name -------------------------------
        if (client.getName() == null || client.getName().trim().isEmpty()) {
            System.out.println("Client name is required.");
            return false;
        }

        // ---------------------------- Nif -------------------------------
        if (client.getNif() == null || !client.getNif().matches("\\d{9}")) {
            System.out.println("NIF must contain exactly 9 digits.");
            return false;
        }

        // ---------------------------- Email -------------------------------
        if (client.getEmail() == null || !client.getEmail().contains("@")) {
            System.out.println("Invalid email.");
            return false;
        }

        // ---------------------------- Phone Number -------------------------------
        if (client.getPhoneNumber() == null ||
            !client.getPhoneNumber().matches("\\d{9}")) {

            System.out.println("Phone number must contain exactly 9 digits.");
            return false;
        }

        return true;
    }

    // ------------------------------- Create Repository ---------------------------------

    private ClientRepository clientRepository;

    public ClientService() {clientRepository = new ClientRepository();}


    // ------------------------------- Create ---------------------------------

    public boolean createClient(Client client) {

    if (!validateClient(client)) {
        return false;
    }

    clientRepository.createClient(client);

    return true;
}

    // ------------------------------- List ---------------------------------

    public List<Client> listClients() {return clientRepository.listClients();}


    // ------------------------------- Find Id ---------------------------------

    public Client findClientID(int id) {

        if (id <= 0) {
            System.out.println("Invalid client ID.");
            return null;
        }
        return clientRepository.findClientID(id);
    }

    // ------------------------------- Update ---------------------------------

    public void updateClient(Client client) {
        
        if (!validateClient(client)) {return;}

        if (client.getId() <= 0) {
            System.out.println("Invalid client ID.");
            return;
        }

        Client existingClient = clientRepository.findClientID(client.getId());

        if (existingClient == null) {
            System.out.println("Client not found.");
            return;
        }

        clientRepository.updateClient(client);
    }


    // ------------------------------- Delete ---------------------------------

    public void deleteClient(int id) {

        if (id <= 0) {
            System.out.println("Invalid client ID.");
            return;
        }

        Client existingClient = clientRepository.findClientID(id);

        if (existingClient == null) {
            System.out.println("Client not found.");
            return;
        }

        clientRepository.deleteClient(id);
    }
}




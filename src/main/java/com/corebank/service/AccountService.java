package com.corebank.service;

import java.util.List;

import com.corebank.model.Account;
import com.corebank.repository.AccountRepository;
import com.corebank.repository.ClientRepository;

public class AccountService {

    private AccountRepository accountRepository;
    private ClientRepository clientRepository;

    public AccountService() {
        accountRepository = new AccountRepository();
        clientRepository = new ClientRepository();
    }


    // ------------------------------ CREATE ------------------------------

    public boolean createAccount(Account account) {

        if (!validateAccount(account)) {
            return false;
        }

        if (clientRepository.findClientID(account.getClientId()) == null) {
            System.out.println("Client not found.");
            return false;
        }

        // New accounts always start with zero balance
        account.setBalance(0);

        accountRepository.createAccount(account);

        return true;
    }

    private boolean validateAccount(Account account) {

        if (account == null) {
            System.out.println("Account data is required.");
            return false;
        }

        if (account.getIban() == null ||
            account.getIban().trim().isEmpty()) {

            System.out.println("IBAN is required.");
            return false;
        }

        if (account.getAccountType() == null ||
            account.getAccountType().trim().isEmpty()) {

            System.out.println("Account type is required.");
            return false;
        }

        if (account.getBalance() < 0) {
            System.out.println("Balance cannot be negative.");
            return false;
        }

        if (account.getStatus() == null ||
            account.getStatus().trim().isEmpty()) {

            System.out.println("Account status is required.");
            return false;
        }

        if (account.getClientId() <= 0) {
            System.out.println("Invalid client ID.");
            return false;
        }

        return true;
    }

    // ------------------------------ LIST ------------------------------

    public List<Account> listAccounts() {
        return accountRepository.listAccounts();
    }


    // ------------------------------ FIND BY ID ------------------------------

    public Account findAccountID(int id) {

        if (id <= 0) {
            System.out.println("Invalid account ID.");
            return null;
        }

        return accountRepository.findAccountID(id);
    }

    // ------------------------------ UPDATE ------------------------------

    public void updateAccount(Account account) {

        if (!validateAccount(account)) {
            return;
        }

        if (account.getId() <= 0) {
            System.out.println("Invalid account ID.");
            return;
        }

        if (accountRepository.findAccountID(account.getId()) == null) {
            System.out.println("Account not found.");
            return;
        }

        if (clientRepository.findClientID(account.getClientId()) == null) {
            System.out.println("Client not found.");
            return;
        }

        accountRepository.updateAccount(account);
    }

    // ------------------------------ DELETE ------------------------------

    public void deleteAccount(int id) {

        if (id <= 0) {
            System.out.println("Invalid account ID.");
            return;
        }

        if (accountRepository.findAccountID(id) == null) {
            System.out.println("Account not found.");
            return;
        }

        accountRepository.deleteAccount(id);
    }














}
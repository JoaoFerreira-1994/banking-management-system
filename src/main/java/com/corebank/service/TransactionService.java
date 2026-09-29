package com.corebank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Account;
import com.corebank.model.Transaction;
import com.corebank.repository.AccountRepository;
import com.corebank.repository.TransactionRepository;

public class TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionService() {
        transactionRepository = new TransactionRepository();
        accountRepository = new AccountRepository();
    }


    // ------------------------------ DEPOSIT ------------------------------

    public boolean deposit(int accountId, double amount) {

        Account account = accountRepository.findAccountID(accountId);

        if (account == null) {
            System.out.println("Account not found.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            System.out.println("Account is not active.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return false;
        }

        double newBalance = account.getBalance() + amount;

       try (Connection conn = DatabaseConfig.connect()) {

        conn.setAutoCommit(false);

        try {

            boolean updated =
                accountRepository.updateBalance(
                    conn,
                    accountId,
                    newBalance
                );

            if (!updated) {
                conn.rollback();
                System.out.println("\nError updating account balance.");
                return false;
            }

            Transaction transaction = new Transaction();

            transaction.setType("DEPOSIT");
            transaction.setAmount(amount);
            transaction.setDate(LocalDateTime.now());
            transaction.setAccountId(accountId);

            transactionRepository.createTransaction(
                conn,
                transaction
            );

            conn.commit();

            return true;

        } catch (SQLException e) {

            conn.rollback();

            System.out.println(
                "\nError processing deposit: " + e.getMessage()
            );

            return false;
        }

        } catch (SQLException e) {

            System.out.println(
                "Database connection error: " + e.getMessage()
            );

            return false;
        }
    }

    // ------------------------------ WITHDRAWAL ------------------------------

    public boolean withdrawal(int accountId, double amount) {

        Account account = accountRepository.findAccountID(accountId);

        if (account == null) {
            System.out.println("Account not found.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            System.out.println("Account is not active.");
            return false;
        }

        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return false;
        }

        if (account.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return false;
        }

        double newBalance = account.getBalance() - amount;

        try (Connection conn = DatabaseConfig.connect()) {

        conn.setAutoCommit(false);

        try {

            boolean updated =
                accountRepository.updateBalance(
                    conn,
                    accountId,
                    newBalance
                );

            if (!updated) {
                conn.rollback();
                System.out.println("Error updating account balance.");
                return false;
            }

            Transaction transaction = new Transaction();

            transaction.setType("WITHDRAWAL");
            transaction.setAmount(amount);
            transaction.setDate(LocalDateTime.now());
            transaction.setAccountId(accountId);

            transactionRepository.createTransaction(
                conn,
                transaction
            );

            conn.commit();

            return true;

            } catch (SQLException e) {

                conn.rollback();

                System.out.println(
                    "Error processing withdrawal: " + e.getMessage()
                );

                return false;
            }

        } catch (SQLException e) {

            System.out.println(
                "Database connection error: " + e.getMessage()
            );

            return false;
        }
    }


    // ------------------------------ LIST ------------------------------

    public List<Transaction> listTransactions() {
        return transactionRepository.listTransactions();
    }


    // ------------------------------ FIND BY ID ------------------------------

    public Transaction findTransactionID(int id) {

        if (id <= 0) {
            System.out.println("Invalid transaction ID.");
            return null;
        }

        return transactionRepository.findTransactionID(id);
    }


    // ------------------------------ LIST BY ACCOUNT ------------------------------

    public List<Transaction> listTransactionsByAccount(int accountId) {

        if (accountId <= 0) {
            System.out.println("Invalid account ID.");
            return List.of();
        }

        return transactionRepository.listTransactionsByAccount(accountId);
    }
}
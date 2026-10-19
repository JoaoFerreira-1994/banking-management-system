package com.corebank.service;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Account;
import com.corebank.model.Client;
import com.corebank.model.Payment;
import com.corebank.model.Transaction;
import com.corebank.repository.AccountRepository;
import com.corebank.repository.ClientRepository;
import com.corebank.repository.PaymentRepository;
import com.corebank.repository.TransactionRepository;


public class PaymentService {

    private AccountRepository accountRepository;
    private PaymentRepository paymentRepository;
    private TransactionRepository transactionRepository;
    private final ClientRepository clientRepository = new ClientRepository();

    public PaymentService() {
        accountRepository = new AccountRepository();
        paymentRepository = new PaymentRepository();
        transactionRepository = new TransactionRepository();
    }

    // ------------------------------ PAYMENT ------------------------------

    public boolean makePayment(
            int accountId,
            String entity,
            String reference,
            double amount) {

        Account account = accountRepository.findAccountID(accountId);

        if (account == null) {
            System.out.println("Account not found.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(account.getStatus())) {
            System.out.println("Account is not active.");
            return false;
        }

        
        Client client = clientRepository.findClientID(account.getClientId());

        if (client == null) {
            System.out.println("Client not found.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(client.getStatus())) {
            System.out.println("Client is not active.");
            return false;
        }


        if (entity == null || entity.trim().isEmpty()) {
            System.out.println("Entity is required.");
            return false;
        }

        if (reference == null || reference.trim().isEmpty()) {
            System.out.println("Reference is required.");
            return false;
        }

        if (!Double.isFinite(amount) || amount <= 0) {
            System.out.println("Invalid payment amount.");
            return false;
        }

        if (account.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return false;
        }

        Connection conn = null;

        try {

            conn = DatabaseConfig.connect();
            conn.setAutoCommit(false);

            double newBalance = account.getBalance() - amount;
            LocalDateTime paymentDate = LocalDateTime.now();

            boolean balanceUpdated =
                accountRepository.updateBalance(
                    conn,
                    accountId,
                    newBalance
                );

            if (!balanceUpdated) {
                throw new SQLException("Error updating account balance.");
            }

            Payment payment = new Payment();

            payment.setAccountId(accountId);
            payment.setEntity(entity);
            payment.setReference(reference);
            payment.setAmount(amount);
            payment.setDate(paymentDate);
            payment.setStatus("COMPLETED");

            paymentRepository.createPayment(conn, payment);

            Transaction transaction = new Transaction();

            transaction.setType("PAYMENT");
            transaction.setAmount(amount);
            transaction.setDate(paymentDate);
            transaction.setAccountId(accountId);

            transactionRepository.createTransaction(conn, transaction);

            conn.commit();

            System.out.println("Payment completed successfully!");
            return true;

        } catch (SQLException e) {

            System.out.println("Payment error: " + e.getMessage());

            if (conn != null) {
                try {
                    conn.rollback();
                    System.out.println("Payment rolled back successfully.");
                } catch (SQLException rollbackError) {
                    System.out.println(
                        "Rollback error: " + rollbackError.getMessage()
                    );
                }
            }

            return false;

        } finally {

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                    System.out.println(
                        "Error closing connection: " + e.getMessage()
                    );
                }
            }
        }
    }

    // ------------------------------ LIST ------------------------------

    public List<Payment> listPayments() {
        return paymentRepository.listPayments();
    }


    // ------------------------------ FIND BY ID ------------------------------

    public Payment findPaymentID(int id) {

        if (id <= 0) {
            System.out.println("Invalid payment ID.");
            return null;
        }

        return paymentRepository.findPaymentID(id);
    }
}
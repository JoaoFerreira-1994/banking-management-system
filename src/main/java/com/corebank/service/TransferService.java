package com.corebank.service;

import com.corebank.config.DatabaseConfig;
import com.corebank.model.Account;
import com.corebank.model.Transfer;
import com.corebank.model.Transaction;
import com.corebank.repository.AccountRepository;
import com.corebank.repository.TransferRepository;
import com.corebank.repository.TransactionRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class TransferService {

    private AccountRepository accountRepository;
    private TransferRepository transferRepository;
    private TransactionRepository transactionRepository;

    public TransferService() {
        accountRepository = new AccountRepository();
        transferRepository = new TransferRepository();
        transactionRepository = new TransactionRepository();
    }


    // ------------------------------ TRANSFER ------------------------------

    public boolean transfer(
            int sourceAccountId,
            int destinationAccountId,
            double amount) {

        // Validate different accounts
        if (sourceAccountId == destinationAccountId) {
            System.out.println(
                "Source and destination accounts must be different."
            );
            return false;
        }

        // Validate amount
        if (amount <= 0) {
            System.out.println("Amount must be greater than zero.");
            return false;
        }

        // Find accounts
        Account sourceAccount =
            accountRepository.findAccountID(sourceAccountId);

        Account destinationAccount =
            accountRepository.findAccountID(destinationAccountId);

        if (sourceAccount == null) {
            System.out.println("Source account not found.");
            return false;
        }

        if (destinationAccount == null) {
            System.out.println("Destination account not found.");
            return false;
        }

        // Validate account status
        if (!"ACTIVE".equalsIgnoreCase(sourceAccount.getStatus())) {
            System.out.println("Source account is not active.");
            return false;
        }

        if (!"ACTIVE".equalsIgnoreCase(destinationAccount.getStatus())) {
            System.out.println("Destination account is not active.");
            return false;
        }

        // Validate balance
        if (sourceAccount.getBalance() < amount) {
            System.out.println("Insufficient balance.");
            return false;
        }


        // ---------------- SQL TRANSACTION ----------------

        Connection conn = null;

        try {

            conn = DatabaseConfig.connect();

            // Disable automatic commits
            conn.setAutoCommit(false);


            // Calculate new balances
            double sourceNewBalance =
                sourceAccount.getBalance() - amount;

            double destinationNewBalance =
                destinationAccount.getBalance() + amount;


            // Update source account
            boolean sourceUpdated =
                accountRepository.updateBalance(
                    conn,
                    sourceAccountId,
                    sourceNewBalance
                );

            if (!sourceUpdated) {
                throw new SQLException(
                    "Error updating source account."
                );
            }


            // Update destination account
            boolean destinationUpdated =
                accountRepository.updateBalance(
                    conn,
                    destinationAccountId,
                    destinationNewBalance
                );

            if (!destinationUpdated) {
                throw new SQLException(
                    "Error updating destination account."
                );
            }


            // Create transfer
            Transfer transfer = new Transfer();

            transfer.setSourceAccountId(sourceAccountId);
            transfer.setDestinationAccountId(destinationAccountId);
            transfer.setAmount(amount);
            transfer.setDate(LocalDateTime.now());
            transfer.setStatus("COMPLETED");

            transferRepository.createTransfer(conn, transfer);


            // Transaction for source account
            Transaction sourceTransaction = new Transaction();

            sourceTransaction.setType("TRANSFER");
            sourceTransaction.setAmount(-amount);
            sourceTransaction.setDate(LocalDateTime.now());
            sourceTransaction.setAccountId(sourceAccountId);

            transactionRepository.createTransaction(
                conn,
                sourceTransaction
            );


            // Transaction for destination account
            Transaction destinationTransaction = new Transaction();

            destinationTransaction.setType("TRANSFER");
            destinationTransaction.setAmount(amount);
            destinationTransaction.setDate(LocalDateTime.now());
            destinationTransaction.setAccountId(destinationAccountId);

            transactionRepository.createTransaction(
                conn,
                destinationTransaction
            );


            // Everything succeeded
            conn.commit();

            System.out.println("Transfer completed successfully!");

            return true;

        } catch (SQLException e) {

            System.out.println(
                "Transfer error: " + e.getMessage()
            );

            if (conn != null) {
                try {

                    conn.rollback();

                    System.out.println(
                        "Transfer rolled back successfully."
                    );

                } catch (SQLException rollbackError) {

                    System.out.println(
                        "Rollback error: "
                        + rollbackError.getMessage()
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
                        "Error closing connection: "
                        + e.getMessage()
                    );
                }
            }
        }
    }
}
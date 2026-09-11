Core - Banking Management System
Description

Core is a banking management system developed in Java, designed to manage customers and bank accounts.

For operations involving payments to external entities, Core will be responsible for managing, validating, and authorizing the operation. The actual payment processing will be performed by an external system through HTTP communication.

Main Entities (Database)

Customer: Represents customers registered in the system. Contains information such as name, tax identification number (NIF), email, phone number, and customer status.

Account: Represents bank accounts belonging to customers. Each account contains information such as IBAN, account type, balance, status, and account holder.

Transaction: Represents operations performed on a bank account, such as deposits, withdrawals, transfers, payments, or other transactions.

Transfer: Represents a money transfer between two accounts, identifying the source account, destination account, amount, date, and operation status.

Main Business Rules

Validate customer and account data before performing operations.
Ensure that only active accounts can perform banking operations.
Verify that sufficient funds are available before debit operations.
Correctly update the account balance after each operation.
Record transactions associated with the operations performed.
Validate the accounts involved before performing transfers.
Apply the limits and restrictions defined for each type of operation.
Ensure data consistency if an operation is not completed successfully.
For external payments, validate and authorize the operation before sending it via HTTP to an external system for processing.

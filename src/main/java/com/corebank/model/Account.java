package com.corebank.model;

public class Account {

    private int id;
    private String iban;
    private String accountType;
    private double balance;
    private String status;
    private int clientId;

    public Account() {
    }

    public Account(
            int id,
            String iban,
            String accountType,
            double balance,
            String status,
            int clientId) {

        this.id = id;
        this.iban = iban;
        this.accountType = accountType;
        this.balance = balance;
        this.status = status;
        this.clientId = clientId;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getIban() {return iban;}
    public void setIban(String iban) {this.iban = iban;}

    public String getAccountType() {return accountType;}
    public void setAccountType(String accountType) {this.accountType = accountType;}

    public double getBalance() {return balance;}
    public void setBalance(double balance) {this.balance = balance;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}

    public int getClientId() {return clientId;}
    public void setClientId(int clientId) {this.clientId = clientId;}
}
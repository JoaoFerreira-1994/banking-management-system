package com.corebank.model;

import java.time.LocalDateTime;

public class Transfer {

    private int id;
    private int sourceAccountId;
    private int destinationAccountId;
    private double amount;
    private LocalDateTime date;
    private String status;


    // ------------------------------ CONSTRUCTORS ------------------------------

    public Transfer() {
    }

    public Transfer(
            int id,
            int sourceAccountId,
            int destinationAccountId,
            double amount,
            LocalDateTime date,
            String status) {

        this.id = id;
        this.sourceAccountId = sourceAccountId;
        this.destinationAccountId = destinationAccountId;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }


    // ------------------------------ GETTERS AND SETTERS ------------------------------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getSourceAccountId() { return sourceAccountId; }
    public void setSourceAccountId(int sourceAccountId) {
        this.sourceAccountId = sourceAccountId;
    }

    public int getDestinationAccountId() { return destinationAccountId; }
    public void setDestinationAccountId(int destinationAccountId) {
        this.destinationAccountId = destinationAccountId;
    }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}

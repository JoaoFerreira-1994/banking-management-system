package com.corebank.model;

import java.time.LocalDateTime;

public class Payment {

    private int id;
    private int accountId;
    private String entity;
    private String reference;
    private double amount;
    private LocalDateTime date;
    private String status;

    public Payment() {
    }

    public Payment(
            int id,
            int accountId,
            String entity,
            String reference,
            double amount,
            LocalDateTime date,
            String status) {

        this.id = id;
        this.accountId = accountId;
        this.entity = entity;
        this.reference = reference;
        this.amount = amount;
        this.date = date;
        this.status = status;
    }

    public int getId() {return id;}
    public void setId(int id){this.id = id;}

    public int getAccountId(){return accountId;}
    public void setAccountId(int accountId) {this.accountId = accountId;}

    public String getEntity(){return entity;}
    public void setEntity(String entity){this.entity = entity;}

    public String getReference(){return reference;}
    public void setReference(String reference){this.reference = reference;}

    public double getAmount(){return amount;}
    public void setAmount(double amount){this.amount = amount;}

    public LocalDateTime getDate(){return date;}
    public void setDate(LocalDateTime date){this.date = date;}

    public String getStatus(){return status;}
    public void setStatus(String status){this.status = status;}
}

package com.corebank.model;

public class Employee {

    private int id;
    private String name;
    private String email;
    private String phoneNumber;
    private String status;

    public Employee() {
    }

    public Employee(
            int id,
            String name,
            String email,
            String phoneNumber,
            String status) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.status = status;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public String getEmail(){return email;}
    public void setEmail(String email){this.email = email;}

    public String getPhoneNumber(){return phoneNumber;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber = phoneNumber;}

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
}
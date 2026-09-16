package com.corebank.model;

public class Client {

    private int id;
    private String name;
    private String email;
    private String nif;
    private String phoneNumber;
    private String status;

    public Client (int id, String name, String nif, String email,  String phoneNumber, String status){
        this.id=id;
        this.name=name;
        this.nif=nif;
        this.email=email;
        this.phoneNumber=phoneNumber;
        this.status=status;
    }

    // -------------------- GETTERS ---------------------
    public int getId(){return id;}
    public String getName(){return name;}
    public String getNif(){return nif;}
    public String getEmail(){return email;}
    public String getPhoneNumber(){return phoneNumber;}
    public String getStatus(){return status;}

    // -------------------- SETTERS ---------------------
    public void setId(int id){this.id=id;}
    public void setName(String name){this.name=name;}
    public void setNif(String nif){this.nif=nif;}
    public void setEmail(String email){this.email=email;}
    public void setPhoneNumber(String phoneNumber){this.phoneNumber=phoneNumber;}
    public void setStatus(String status){this.status=status;}
}

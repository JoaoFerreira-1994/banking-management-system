package com.corebank.model;

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String nif;
    private String telefone;

    public Cliente(int id, String nome, String nif, String email,  String telefone){
        this.id=id;
        this.nome=nome;
        this.nif=nif;
        this.email=email;
        this.telefone=telefone;
    }

    // -------------------- GET ---------------------
    public int getId(){return id;}
    public String getNome(){return nome;}
    public String getNif(){return nif;}
    public String getEmail(){return email;}
    public String getTelefone(){return telefone;}

    // -------------------- SET ---------------------
    public void setId(int id){this.id=id;}
    public void setNome(String nome){this.nome=nome;}
    public void setNif(String nif){this.nif=nif;}
    public void setEmail(String email){this.email=email;}
    public void setTelefone(String telefone){this.telefone=telefone;}
}

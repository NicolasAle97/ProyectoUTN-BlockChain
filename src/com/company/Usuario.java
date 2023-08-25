package com.company;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Usuario implements Serializable {

    private static int generalId;
    private int id;
    private String mail;
    private String contraseña;
    private String dni;
    private String nombre;
    private Wallet wallet;
    /* Owner reference must be here and be set in te constructor just once */


    public Usuario( String nombre,String mail, String contraseña, String dni) {
        setGeneralId();
        this.id = generalId;
        this.mail = mail;
        this.contraseña = contraseña;
        this.dni = dni;
        this.nombre=nombre;
        setWallet();
    }
    public Usuario(){

    }

    //GETTERS AND SETTERS
    public static int getGeneralId() {
        return generalId;
    }
    public static void setGeneralId() {
        generalId = generalId+1;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getMail() {
        return mail;
    }
    public void setMail(String mail) {
        this.mail = mail;
    }
    public String getContraseña() {
        return contraseña;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
    public String getDni() {
        return this.dni;
    }
    public void setDni(String dni) {
        this.dni = dni;
    }
    public Wallet getWallet() {
        return wallet;
    }
    public void setWallet() {
        Wallet wallet=new Wallet(generateUserReference());
        this.wallet = wallet;
    }

    //METHODS
    private int generateUserReference(){
        Random numAleatorio=new Random();
        int n= numAleatorio.nextInt(5000-2000+1)+2000;
        //System.out.println("USER REFERENCE : "+n);
        return n;
    }

    @Override
    public String toString() {
        return "[Usuario] " + "\n"+
                " | ID:" + id +
                " | Mail:" + mail +
                " | Password:" + contraseña +
                " | DNI:" + dni +
                " | Nombre:" + nombre +
                "\n | " + wallet.toString() +
                " \n ";
    }
}

package com.company;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

public class Transaction implements Serializable {
    private static int identificador;
    private int id;
    private HashMap<String, Boolean> validators=new HashMap<>();
    private int amount;
    private Wallet sender;
    private Wallet recieber;
    //private LocalDate fechaCreacion = null;
    public Transaction(Wallet recieber, Wallet sender, HashMap<String,Boolean> validators, int amount, int id) {
        this.sender=sender;
        this.recieber=recieber;
        this.amount=amount;
        this.id=id;
        this.validators=validators;
        //fechaCreacion=LocalDate.now();
    }

    public Transaction(){

    }
    //GETTERS AND SETTERS
    public int getId() {
        return this.id;
    }
    public void setId() {
        this.id =identificador;
    }
    public void setIdentificador() {
        identificador = identificador + 1;
    }
    public HashMap<String, Boolean> getValidators() {
        return validators;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
    public Wallet getSender() {
        return sender;
    }
    public void setSender(Wallet sender) {
        this.sender = sender;
    }
    public Wallet getRecieber() {
        return recieber;
    }
    public void setRecieber(Wallet recieber) {
        this.recieber = recieber;
    }

    public void setValidators(HashMap<String, Boolean> validators) {
        this.validators = validators;
    }
    //METHODS

    public void updateValidation(String dni){
        validators.remove(dni);
        validators.put(dni,true);
    }
    public void updateAllValidatorsToTrue(){
        HashMap<String,Boolean>aux=new HashMap<>();
        for (String doc:validators.keySet()) {
            aux.put(doc,true);
        }
    this.validators=aux;
    }

    //public LocalDate getFechaCreacion() {
    //    return fechaCreacion;
    //}

   // public void setFechaCreacion(LocalDate fechaCreacion) {
   //     this.fechaCreacion = fechaCreacion;
   // }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", validators=" + validators +
                ", amount=" + amount +
                ", sender=" + sender +
                ", recieber=" + recieber +
                '}';
    }
}

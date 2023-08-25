package com.company;

import java.io.Serializable;

public class Wallet implements Serializable {

    private double utnCoins = 100;
    private double money = 0;
    private int ownerReference;


    public Wallet(int ownerReference) {
        this.ownerReference = ownerReference;
    }

    public Wallet() {

    }

    //GETTERS AND SETTERS
    public double getUtnCoins() {
        return utnCoins;
    }

    public void setUtnCoins(double utnCoins) {
        this.utnCoins = utnCoins;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getOwnerReference() {
        return ownerReference;
    }

    public void setOwnerReference(int ownerReference) {
        this.ownerReference = ownerReference;
    }

    @Override
    public String toString() {
        return "[Wallet] " +
                " | Coin: UTN$" + utnCoins +
                " | Money: $" + money +
                " | Owner Reference:" + ownerReference + " ";
    }
}

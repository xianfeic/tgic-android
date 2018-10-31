package com.d8sense.tgic.ark.core;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Account implements Serializable{
    public String publicKey;
    public long   balance;
    public String username;
    long vote;
    List votes;
    int rate;

    public Account(String address)
    {
//        this.address = address;
    }

    public boolean applyTransaction(Transaction transaction) {
        balance -= transaction.amount + transaction.fee;
        return balance > -1;
    }


    public boolean undoTransaction(Transaction transaction){
        balance += transaction.amount + transaction.fee;
        return balance > -1;
    }

    public Verification verifyTransaction(Transaction transaction){
        Verification v = new Verification();
        if(balance < transaction.amount + transaction.fee)
            v.errors.add("Account ${address} does not have enough balance: ${balance}");
        // TODO: many things

        return v;

    }

}

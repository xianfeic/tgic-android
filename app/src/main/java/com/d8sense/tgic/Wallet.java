package com.d8sense.tgic;

import com.d8sense.tgic.ark.core.Account;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jason.z on 2018/9/15.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Wallet implements Serializable {
    String secondPublicKey;
    long balance;
    String username;
    String vote;
    private String address;
    private String publicKey;
    private String qrAddress;
    private String entropy;
    private List<String> mnemonic;
    private String secondMnemonic;
    private String seed;
    private String wif;

    Wallet()
    {

    }

    @Override
    public boolean equals(Object arg0) {
        Wallet wallet = (Wallet) arg0;
        return address.equals(wallet.address);
    }

    public void setAddress(String address){
        this.address = address;
    }

    public void setEntropy(String entropy){
        this.entropy = entropy;
    }

    public void setMnemonic(List<String> mnemonic){
        this.mnemonic = mnemonic;
    }

    public void setPublicKey(String publicKey){
        this.publicKey = publicKey;
    }

    public void setSeed(String seed){
        this.seed = seed;
    }

    public void setWif(String wif) {
        this.wif = wif;
    }

    public String getAddress() {
        return this.address;
    }

    public List<String> getMnemonic() {
        return this.mnemonic;
    }

    public String getPublicKey() {
        return this.publicKey;
    }


}

package com.d8sense.tgic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Jason.z on 2018/9/23.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class AccountBackup implements Serializable {
    private String address;
    private String publicKey;
    private String qrAddress;
    private String entropy;
    private List<String> mnemonic;
    private String secondMnemonic;
    private String seed;
    private String wif;

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

}

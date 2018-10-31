package com.d8sense.tgic.ark.crypto.networks;

public class Testnet implements INetwork {

    public int version() {
        return 23;
    }

    public int wif() {
        return 186;
    }

    public String epoch() {
        return "2017-03-21 13:00:00";
    }

}

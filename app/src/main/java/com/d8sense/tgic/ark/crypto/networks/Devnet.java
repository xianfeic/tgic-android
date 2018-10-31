package com.d8sense.tgic.ark.crypto.networks;

public class Devnet implements INetwork {

    public int version() {
        return 30;
    }

    public int wif() {
        return 170;
    }

    public String epoch() {
        return "2017-03-21 13:00:00";
    }

}

package com.d8sense.tgic.ark.crypto.identities;

public class PublicKey {
    public static String fromPassphrase(String passphrase) {
        return PrivateKey.fromPassphrase(passphrase).getPublicKeyAsHex();
    }

}

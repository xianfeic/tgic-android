package com.d8sense.tgic.ark.crypto.identities;

import com.google.common.primitives.Bytes;
import com.d8sense.tgic.ark.crypto.configuration.Network;
import com.d8sense.tgic.ark.crypto.encoding.Base58;
import com.d8sense.tgic.ark.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

public class Address {
    public static String fromPassphrase(String passphrase, Integer networkVersion) {
        return fromPrivateKey(PrivateKey.fromPassphrase(passphrase), networkVersion);
    }

    public static String fromPassphrase(String passphrase) {
        return Address.fromPassphrase(passphrase, null);
    }

    public static String fromPublicKey(String publicKey, Integer networkVersion) {
        byte[] publicKeyBytes = Hex.decode(publicKey);

        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(publicKeyBytes, 0, publicKeyBytes.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);

        if (networkVersion == null) {
            networkVersion = Network.get().version();
        }

        byte[] bytes = Bytes.concat(new byte[]{networkVersion.byteValue()}, out);
        return Base58.encodeChecked(bytes);
    }

    public static String fromPublicKey(String publicKey) {
        return Address.fromPublicKey(publicKey, null);
    }

    public static String fromPrivateKey(ECKey privateKey, Integer networkVersion) {
        return fromPublicKey(privateKey.getPublicKeyAsHex(), networkVersion);
    }

    public static String fromPrivateKey(ECKey privateKey) {
        return Address.fromPrivateKey(privateKey, null);
    }

    public static Boolean validate(String address, Integer networkVersion) {
        if (networkVersion == null) {
            networkVersion = Network.get().version();
        }


        return Base58.decodeChecked(address)[0] == networkVersion;
    }

    public static Boolean validate(String address) {
        return Address.validate(address, null);
    }

}

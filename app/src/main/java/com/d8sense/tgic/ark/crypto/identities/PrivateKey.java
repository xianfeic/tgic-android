package com.d8sense.tgic.ark.crypto.identities;

import com.d8sense.tgic.ark.crypto.encoding.Hex;
import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;

public class PrivateKey {
    public static ECKey fromPassphrase(String passphrase) {
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes());

        return ECKey.fromPrivate(sha256, true);
    }

    public static ECKey fromHex(String privateKey) {
        return ECKey.fromPrivate(Hex.decode(privateKey), true);
    }

}

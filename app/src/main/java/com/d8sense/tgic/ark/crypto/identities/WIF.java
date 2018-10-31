package com.d8sense.tgic.ark.crypto.identities;

import com.d8sense.tgic.ark.crypto.configuration.Network;
import com.d8sense.tgic.ark.crypto.encoding.Base58;
import org.bitcoinj.core.Sha256Hash;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class WIF {
    public static String fromPassphrase(String passphrase) throws IOException {
        byte[] secret = Sha256Hash.hash(passphrase.getBytes());

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(Network.get().wif());
        outputStream.write(secret);
        outputStream.write(0x01);

        return Base58.encodeChecked(outputStream.toByteArray());
    }

}

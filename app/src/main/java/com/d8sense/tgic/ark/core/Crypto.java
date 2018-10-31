package com.d8sense.tgic.ark.core;

import com.google.common.io.BaseEncoding;

import org.bitcoinj.core.*;
import org.spongycastle.crypto.digests.RIPEMD160Digest;

/**
 * Created by Jason.z on 2018/9/24.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public class Crypto {

    static int networkVersion = 0x17;


    static ECKey.ECDSASignature sign(Transaction t, String passphrase){
        byte[] txbytes = getBytes(t,true,true);
        return signBytes(txbytes, passphrase);
    }

    static ECKey.ECDSASignature secondSign(Transaction t, String secondPassphrase){
        byte[] txbytes = getBytes(t, false,true);
        return signBytes(txbytes, secondPassphrase);
    }

    static ECKey.ECDSASignature signBytes(byte[] bytes, String passphrase){
        ECKey keys = getKeys(passphrase);
        return keys.sign(Sha256Hash.of(bytes));
    }

    static boolean verify(Transaction t){
        ECKey keys = ECKey.fromPublicOnly(BaseEncoding.base16().lowerCase().decode(t.senderPublicKey));
        byte[] signature = BaseEncoding.base16().lowerCase().decode(t.signature);
        byte[] bytes = getBytes(t,true,true);
        return verifyBytes(bytes, signature, keys.getPubKey());
    }

    static boolean secondVerify(Transaction t, String secondPublicKeyHex){
        ECKey keys = ECKey.fromPublicOnly(BaseEncoding.base16().lowerCase().decode(secondPublicKeyHex));
        byte[] signature = BaseEncoding.base16().lowerCase().decode(t.signSignature);
        byte[] bytes = getBytes(t, false,true);
        return verifyBytes(bytes, signature, keys.getPubKey());
    }

    static boolean verifyBytes(byte[] bytes, byte[] signature, byte[] publicKey){
        return ECKey.verify(Sha256Hash.hash(bytes), signature, publicKey);
    }

    static byte[] getBytes(Transaction t, boolean skipSignature, boolean skipSecondSignature){
        return t.toBytes(skipSignature, skipSecondSignature);
    }

    static String getId(Transaction t){
        return BaseEncoding.base16().lowerCase().encode(Sha256Hash.hash(getBytes(t, false, false)));
    }

    static ECKey getKeys(String passphrase){
        byte[] sha256 = Sha256Hash.hash(passphrase.getBytes());
        ECKey keys = ECKey.fromPrivate(sha256, true);
        return keys;
    }

    static String getAddress(ECKey keys){
        return getAddress(keys.getPubKey());
    }

    static String getAddress(byte[] publicKey){
        RIPEMD160Digest digest = new RIPEMD160Digest();
        digest.update(publicKey, 0, publicKey.length);
        byte[] out = new byte[20];
        digest.doFinal(out, 0);
        VersionedChecksummedBytes2 address = new VersionedChecksummedBytes2(networkVersion, out);
        return address.toBase58();
    }
}

class VersionedChecksummedBytes2  extends VersionedChecksummedBytes{

    public VersionedChecksummedBytes2(int version, byte[] bytes) {
        super(version, bytes);
    }
}




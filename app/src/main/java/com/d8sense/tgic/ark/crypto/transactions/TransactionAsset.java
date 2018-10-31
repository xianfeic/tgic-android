package com.d8sense.tgic.ark.crypto.transactions;

import java.util.ArrayList;
import java.util.List;

public class TransactionAsset {
    public Signature signature = new Signature();
    public List<String> votes = new ArrayList<>();
    public Delegate delegate = new Delegate();
    public MultiSignature multisignature = new MultiSignature();

    public class Signature {
        public String publicKey;
    }

    public class Delegate {
        public String username;
        public String publicKey;
    }

    public class MultiSignature {
        public byte min;
        public byte lifetime;
        public List<String> keysgroup = new ArrayList<>();
    }
}

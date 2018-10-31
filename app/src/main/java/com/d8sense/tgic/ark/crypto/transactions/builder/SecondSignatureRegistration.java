package com.d8sense.tgic.ark.crypto.transactions.builder;

import com.d8sense.tgic.ark.crypto.encoding.Hex;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.identities.PublicKey;

public class SecondSignatureRegistration extends AbstractTransaction {

    public SecondSignatureRegistration signature(String signature) {
        this.transaction.asset.signature.publicKey = Hex.encode(PublicKey.fromPassphrase(signature).getBytes());

        return this;
    }

    public Types getType() {
        return Types.SECOND_SIGNATURE_REGISTRATION;
    }

}

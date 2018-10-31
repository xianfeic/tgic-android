package com.d8sense.tgic.ark.crypto.transactions.serializers;

import com.d8sense.tgic.ark.crypto.encoding.Hex;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class SecondSignatureRegistration extends AbstractSerializer {
    public SecondSignatureRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        this.buffer.put(Hex.decode(this.transaction.asset.signature.publicKey));
    }

}

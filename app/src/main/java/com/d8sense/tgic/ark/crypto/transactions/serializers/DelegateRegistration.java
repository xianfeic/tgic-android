package com.d8sense.tgic.ark.crypto.transactions.serializers;

import com.d8sense.tgic.ark.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class DelegateRegistration extends AbstractSerializer {
    public DelegateRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        byte[] delegateBytes = this.transaction.asset.delegate.username.getBytes();

        this.buffer.put((byte) delegateBytes.length);
        this.buffer.put(delegateBytes);
    }

}

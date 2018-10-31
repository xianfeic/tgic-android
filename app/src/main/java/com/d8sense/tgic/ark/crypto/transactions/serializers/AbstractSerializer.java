package com.d8sense.tgic.ark.crypto.transactions.serializers;

import com.d8sense.tgic.ark.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public abstract class AbstractSerializer {
    protected ByteBuffer buffer;
    protected Transaction transaction;

    public AbstractSerializer(ByteBuffer buffer, Transaction transaction) {
        this.buffer = buffer;
        this.transaction = transaction;
    }

}

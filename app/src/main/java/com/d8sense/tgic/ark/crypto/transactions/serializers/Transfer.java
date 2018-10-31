package com.d8sense.tgic.ark.crypto.transactions.serializers;

import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import org.bitcoinj.core.Base58;

import java.nio.ByteBuffer;

public class Transfer extends AbstractSerializer {
    public Transfer(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        this.buffer.putLong(this.transaction.amount);
        this.buffer.putInt(this.transaction.expiration);
        this.buffer.put(Base58.decodeChecked(this.transaction.recipientId));
    }

}

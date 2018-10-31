package com.d8sense.tgic.ark.crypto.transactions.deserializers;

import com.d8sense.tgic.ark.crypto.encoding.Base58;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class Transfer extends AbstractDeserializer {
    public Transfer(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);

        this.transaction.amount = this.buffer.getLong();
        this.transaction.expiration = this.buffer.getInt();

        byte[] recipientId = new byte[21];
        this.buffer.get(recipientId);
        this.transaction.recipientId = Base58.encodeChecked(recipientId);

        this.transaction.parseSignatures(this.serialized, assetOffset + (8 + 4 + 21) * 2);
    }

}

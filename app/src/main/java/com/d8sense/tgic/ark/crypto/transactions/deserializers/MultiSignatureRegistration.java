package com.d8sense.tgic.ark.crypto.transactions.deserializers;

import com.d8sense.tgic.ark.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class MultiSignatureRegistration extends AbstractDeserializer {
    public MultiSignatureRegistration(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);

        this.transaction.asset.multisignature.min = (byte) (this.buffer.get() & 0xff);

        int count = this.buffer.get() & 0xff;
        this.transaction.asset.multisignature.lifetime = (byte) (this.buffer.get() & 0xff);

        for (int i = 0; i < count; i++) {
            String key = this.serialized.substring(assetOffset + 6 + i * 66, assetOffset + 6 + (i + 1) * 66);
            this.transaction.asset.multisignature.keysgroup.add(key);
        }

        this.transaction.parseSignatures(this.serialized, assetOffset + 6 + count * 66);
    }

}

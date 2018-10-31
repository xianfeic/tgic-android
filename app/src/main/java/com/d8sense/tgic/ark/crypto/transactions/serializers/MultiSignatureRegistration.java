package com.d8sense.tgic.ark.crypto.transactions.serializers;

import com.d8sense.tgic.ark.crypto.encoding.Hex;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.d8sense.tgic.ark.crypto.transactions.TransactionAsset;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class MultiSignatureRegistration extends AbstractSerializer {
    public MultiSignatureRegistration(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        List keysgroup = new ArrayList();

        TransactionAsset.MultiSignature multiSignature = this.transaction.asset.multisignature;

        for (int i = 0; i < multiSignature.keysgroup.size(); i++) {
            String key = multiSignature.keysgroup.get(i);

            if (key.startsWith("+")) {
                multiSignature.keysgroup.set(i, key.substring(1));
            }
        }

        this.buffer.put(multiSignature.min);
        this.buffer.put((byte) multiSignature.keysgroup.size());
        this.buffer.put(multiSignature.lifetime);
        this.buffer.put(Hex.decode(String.join("", multiSignature.keysgroup)));
    }

}

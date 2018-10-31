package com.d8sense.tgic.ark.crypto.transactions.serializers;

import com.d8sense.tgic.ark.crypto.encoding.Hex;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.google.common.base.Joiner;

import java.nio.ByteBuffer;
import java.util.List;

public class Vote extends AbstractSerializer {
    public Vote(ByteBuffer buffer, Transaction transaction) {
        super(buffer, transaction);
    }

    public void serialize() {
        List<String> votes = transaction.asset.votes;

        for (int i = 0; i < votes.size(); i++) {
            votes.set(i, (votes.get(i).startsWith("+") ? "01" : "00") + votes.get(i).substring(1));
        }

        this.buffer.put((byte) votes.size());
        this.buffer.put(Hex.decode(Joiner.on(" ").join(votes)));
    }

}

package com.d8sense.tgic.ark.crypto.transactions.deserializers;

import com.d8sense.tgic.ark.crypto.transactions.Transaction;

import java.nio.ByteBuffer;

public class Vote extends AbstractDeserializer {
    public Vote(String serialized, ByteBuffer buffer, Transaction transaction) {
        super(serialized, buffer, transaction);
    }

    public void deserialize(int assetOffset) {
        this.buffer.position(assetOffset / 2);
        int voteLength = this.buffer.get() & 0xff;

        for (int i = 0; i < voteLength; i++) {
            String vote = this.serialized.substring(assetOffset + 2 + i * 2 * 34, assetOffset + 2 + (i + 1) * 2 * 34);
            vote = (vote.startsWith("01") ? '+' : '-') + vote.substring(2);
            transaction.asset.votes.add(vote);
        }

        this.transaction.parseSignatures(this.serialized, assetOffset + 2 + voteLength * 34 * 2);
    }

}

package com.d8sense.tgic.ark.crypto.transactions.builder;

import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.identities.Address;

import java.util.List;

public class Vote extends AbstractTransaction {
    public Vote votes(List votes) {
        this.transaction.asset.votes = votes;

        return this;
    }

    public Vote sign(String passphrase) {
        this.transaction.recipientId = Address.fromPassphrase(passphrase);

        super.sign(passphrase);

        return this;
    }

    public Types getType() {
        return Types.VOTE;
    }

}

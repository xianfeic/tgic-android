package com.d8sense.tgic.ark.crypto.transactions.builder;

import com.d8sense.tgic.ark.crypto.configuration.Fee;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.transactions.Transaction;
import com.d8sense.tgic.ark.crypto.utils.Slot;

public abstract class AbstractTransaction {
    public Transaction transaction;

    public AbstractTransaction() {
        this.transaction = new Transaction();
        this.transaction.type = this.getType();
        this.transaction.fee = Fee.get(this.getType());
        this.transaction.timestamp = Slot.time();
    }

    public AbstractTransaction sign(String passphrase) {
        this.transaction.sign(passphrase);
        this.transaction.id = this.transaction.computeId();

        return this;
    }

    public AbstractTransaction secondSign(String passphrase) {
        this.transaction.secondSign(passphrase);
        this.transaction.id = this.transaction.computeId();

        return this;
    }

    abstract Types getType();

}

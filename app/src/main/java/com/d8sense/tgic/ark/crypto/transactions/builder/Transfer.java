package com.d8sense.tgic.ark.crypto.transactions.builder;

import com.d8sense.tgic.ark.crypto.enums.Types;

public class Transfer extends AbstractTransaction {

    public Transfer recipient(String recipientId) {
        this.transaction.recipientId = recipientId;

        return this;
    }

    public Transfer amount(int amount) {
        return this.amount((long) amount);
    }

    public Transfer amount(long amount) {
        this.transaction.amount = amount;

        return this;
    }

    public Transfer vendorField(String vendorField) {
        this.transaction.vendorField = vendorField;

        return this;
    }

    public Types getType() {
        return Types.TRANSFER;
    }

}

package com.d8sense.tgic.ark.crypto.transactions.builder;

import com.d8sense.tgic.ark.crypto.enums.Types;

import java.util.List;


public class MultiSignatureRegistration extends AbstractTransaction {

    public MultiSignatureRegistration min(int min) {
        return this.min((byte) min);
    }

    public MultiSignatureRegistration min(byte min) {
        this.transaction.asset.multisignature.min = min;

        return this;
    }

    public MultiSignatureRegistration lifetime(int lifetime) {
        return this.lifetime((byte) lifetime);
    }

    public MultiSignatureRegistration lifetime(byte lifetime) {
        this.transaction.asset.multisignature.lifetime = lifetime;

        return this;
    }

    public MultiSignatureRegistration keysgroup(List<String> keysgroup) {
        this.transaction.asset.multisignature.keysgroup = keysgroup;

        this.transaction.fee = (keysgroup.size() + 1) * this.transaction.fee;

        return this;
    }

    public Types getType() {
        return Types.TRANSFER;
    }

}

package com.d8sense.tgic.ark.crypto.transactions;

import com.d8sense.tgic.ark.crypto.encoding.Hex;
import com.d8sense.tgic.ark.crypto.enums.Types;
import com.d8sense.tgic.ark.crypto.identities.Address;
import com.d8sense.tgic.ark.crypto.transactions.deserializers.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Deserializer {

    private String serialized;
    private ByteBuffer buffer;
    private Transaction transaction;

    public Transaction deserialize(String serialized) {
        this.serialized = serialized;

        this.buffer = ByteBuffer.wrap(Hex.decode(serialized)).slice();
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);
        this.buffer.get();

        this.transaction = new Transaction();

        int assetOffset = deserializeHeader();
        deserializeTypeSpecific(assetOffset);
        deserializeVersionOne();

        return this.transaction;
    }

    public int deserializeHeader() {
        transaction.version = this.buffer.get();
        transaction.network = this.buffer.get();
        transaction.type = Types.values()[this.buffer.get()];
        transaction.timestamp = this.buffer.getInt();

        byte[] senderPublicKey = new byte[33];
        this.buffer.get(senderPublicKey);
        transaction.senderPublicKey = Hex.encode(senderPublicKey);

        transaction.fee = this.buffer.getLong();

        int vendorFieldLength = this.buffer.get();
        if (vendorFieldLength > 0) {
            byte[] vendorFieldHex = new byte[vendorFieldLength];
            this.buffer.get(vendorFieldHex);
            transaction.vendorFieldHex = Hex.encode(vendorFieldHex);
        }

        return (41 + 8 + 1) * 2 + vendorFieldLength * 2;
    }

    public void deserializeTypeSpecific(int assetOffset) {
        switch (transaction.type) {
            case TRANSFER:
                new Transfer(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case SECOND_SIGNATURE_REGISTRATION:
                new SecondSignatureRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case DELEGATE_REGISTRATION:
                new DelegateRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case VOTE:
                new Vote(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
            case MULTI_SIGNATURE_REGISTRATION:
                new MultiSignatureRegistration(this.serialized, this.buffer, this.transaction).deserialize(assetOffset);
                break;
        }
    }

    public void deserializeVersionOne() {
        if (transaction.secondSignature != null) {
            transaction.signSignature = transaction.secondSignature;
        }

        if (transaction.type == Types.VOTE) {
            transaction.recipientId = Address.fromPublicKey(transaction.senderPublicKey, transaction.network);
        }

        if (transaction.type == Types.MULTI_SIGNATURE_REGISTRATION) {
            for (int i = 0; i < transaction.asset.multisignature.keysgroup.size(); i++) {
                transaction.asset.multisignature.keysgroup.set(i, "+" + transaction.asset.multisignature.keysgroup.get(i));
            }
        }

        if (transaction.vendorFieldHex != null) {
            transaction.vendorField = new String(Hex.decode(transaction.vendorFieldHex));
        }

        if (transaction.id == null) {
            transaction.id = transaction.computeId();
        }

        if (transaction.type == Types.SECOND_SIGNATURE_REGISTRATION || transaction.type == Types.MULTI_SIGNATURE_REGISTRATION) {
            transaction.recipientId = Address.fromPublicKey(transaction.senderPublicKey, transaction.network);
        }

    }

}

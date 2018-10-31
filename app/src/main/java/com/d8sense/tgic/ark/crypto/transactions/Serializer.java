package com.d8sense.tgic.ark.crypto.transactions;

import com.d8sense.tgic.ark.crypto.configuration.Network;
import com.d8sense.tgic.ark.crypto.encoding.Hex;
import com.d8sense.tgic.ark.crypto.transactions.serializers.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Serializer {

    private ByteBuffer buffer;
    private Transaction transaction;

    public byte[] serialize(Transaction transaction) {
        this.transaction = transaction;

        this.buffer = ByteBuffer.allocate(512);
        this.buffer.order(ByteOrder.LITTLE_ENDIAN);

        serializeHeader();
        serializeTypeSpecific();
        serializeSignatures();

        byte[] result = new byte[this.buffer.position()];
        this.buffer.rewind();
        this.buffer.get(result);

        return result;
    }

    private void serializeHeader() {
        this.buffer.put((byte) 0xff);

        if (this.transaction.version > 0) {
            this.buffer.put((byte) this.transaction.version);
        } else {
            this.buffer.put((byte) 0x01);
        }

        if (this.transaction.network > 0) {
            this.buffer.put((byte) this.transaction.network);
        } else {
            this.buffer.put((byte) Network.get().version());
        }

        this.buffer.put((byte) this.transaction.type.getValue());
        this.buffer.putInt(this.transaction.timestamp);
        this.buffer.put(Hex.decode(this.transaction.senderPublicKey));
        this.buffer.putLong(this.transaction.fee);

        if (this.transaction.vendorField != null) {
            int vendorFieldLength = this.transaction.vendorField.length();

            this.buffer.put((byte) vendorFieldLength);
            this.buffer.put(this.transaction.vendorField.getBytes());
        } else if (this.transaction.vendorFieldHex != null) {
            int vendorFieldHexLength = this.transaction.vendorFieldHex.length();

            this.buffer.put((byte) (vendorFieldHexLength / 2));
            this.buffer.put(Hex.decode(this.transaction.vendorFieldHex));
        } else {
            this.buffer.put((byte) 0x00);
        }

    }

    private void serializeTypeSpecific() {
        switch (transaction.type) {
            case TRANSFER:
                new Transfer(this.buffer, this.transaction).serialize();
                break;
            case SECOND_SIGNATURE_REGISTRATION:
                new SecondSignatureRegistration(this.buffer, this.transaction).serialize();
                break;
            case DELEGATE_REGISTRATION:
                new DelegateRegistration(this.buffer, this.transaction).serialize();
                break;
            case VOTE:
                new Vote(this.buffer, this.transaction).serialize();
                break;
            case MULTI_SIGNATURE_REGISTRATION:
                new MultiSignatureRegistration(this.buffer, this.transaction).serialize();
                break;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private void serializeSignatures() {
        if (this.transaction.signature != null) {
            buffer.put(Hex.decode(this.transaction.signature));
        }

        if (this.transaction.secondSignature != null) {
            buffer.put(Hex.decode(this.transaction.secondSignature));
        } else if (this.transaction.signSignature != null) {
            buffer.put(Hex.decode(this.transaction.signSignature));
        }

        if (this.transaction.signatures != null) {
            this.buffer.put((byte) 0xff);
            buffer.put(Hex.decode(String.join("", this.transaction.signatures)));
        }
    }

}

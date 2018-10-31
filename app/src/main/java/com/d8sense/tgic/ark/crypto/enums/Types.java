package com.d8sense.tgic.ark.crypto.enums;

public enum Types {
    TRANSFER(0),
    SECOND_SIGNATURE_REGISTRATION(1),
    DELEGATE_REGISTRATION(2),
    VOTE(3),
    MULTI_SIGNATURE_REGISTRATION(4),
    IPFS(5),
    TIMELOCK_TRANSFER(6),
    MULTI_PAYMENT(7),
    DELEGATE_RESIGNATION(8);

    private final int value;

    Types(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

package com.d8sense.tgic.ark.core;

/**
 * Created by Jason.z on 2018/10/1.
 * Copyright (c) 2016 BeikePay. All rights reserved.
 */
public enum TransactionType {
    NORMAL(0),
    SECONDSIGNATURE(1),
    DELEGATE(2),
    VOTE(3),

    /**
     * @deprecated use SECONDSIGNATURE
     */
    @Deprecated SECONDSIGNITURE(1);

    private final int value;

    TransactionType(int value)
    {
        this.value = (byte)value;
    }

    byte getByteValue() {
        return (byte) value;
    }
}

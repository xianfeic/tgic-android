package com.d8sense.tgic.ark.crypto.configuration;

import com.d8sense.tgic.ark.crypto.enums.Fees;
import com.d8sense.tgic.ark.crypto.enums.Types;

import java.util.HashMap;
import java.util.Map;

public class Fee {
    private static Map<Types, Long> fees = new HashMap<>();

    static {
        fees.put(Types.TRANSFER, Fees.TRANSFER.getValue());
        fees.put(Types.SECOND_SIGNATURE_REGISTRATION, Fees.SECOND_SIGNATURE_REGISTRATION.getValue());
        fees.put(Types.DELEGATE_REGISTRATION, Fees.DELEGATE_REGISTRATION.getValue());
        fees.put(Types.VOTE, Fees.VOTE.getValue());
        fees.put(Types.MULTI_SIGNATURE_REGISTRATION, Fees.MULTI_SIGNATURE_REGISTRATION.getValue());
        fees.put(Types.IPFS, Fees.IPFS.getValue());
        fees.put(Types.TIMELOCK_TRANSFER, Fees.TIMELOCK_TRANSFER.getValue());
        fees.put(Types.MULTI_PAYMENT, Fees.MULTI_PAYMENT.getValue());
        fees.put(Types.DELEGATE_RESIGNATION, Fees.DELEGATE_RESIGNATION.getValue());
    }

    public static long get(Types type) {
        return fees.get(type);
    }

    public static void set(Types type, long fee) {
        fees.put(type, fee);
    }
}

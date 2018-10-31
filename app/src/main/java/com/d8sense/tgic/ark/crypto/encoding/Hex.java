package com.d8sense.tgic.ark.crypto.encoding;

import com.google.common.io.BaseEncoding;

public class Hex {
    public static String encode(byte[] value) {
        return BaseEncoding.base16().lowerCase().encode(value);
    }

    public static byte[] decode(String value) {
        return BaseEncoding.base16().lowerCase().decode(value);
    }

}

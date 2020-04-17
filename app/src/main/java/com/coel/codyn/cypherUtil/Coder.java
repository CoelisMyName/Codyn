package com.coel.codyn.cypherUtil;

import android.util.Base64;

import java.util.regex.Pattern;
/*
这是编码器接口
编码将byte串转成String
解码将String转成String或byte
 */

public interface Coder {

    static boolean isBase64(String str) {
        final String base64Pattern = "^([A-Za-z0-9+/]{4})*([A-Za-z0-9+/]{4}|[A-Za-z0-9+/]{3}=|[A-Za-z0-9+/]{2}==)$";
        return Pattern.matches(base64Pattern, str);
    }

    static String Base64_decode2text(String str) throws IllegalArgumentException {
        return new String(Base64.decode(str, Base64.NO_WRAP));
    }

    static byte[] Base64_decode2bin(String str) throws IllegalArgumentException {
        return Base64.decode(str, Base64.NO_WRAP);
    }

    static String Base64_encode2text(byte[] bytes) {
        return Base64.encodeToString(bytes, Base64.NO_WRAP);
    }

}

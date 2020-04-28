package com.coel.codyn.cypherUtil;

import java.security.KeyStore;

/*
用于提供系统级加密，未来用于加密数据库内容
 */

public class SystemCrypto {
    private final static String KEYSTORE_ALIAS = "KEYSTORE_DEMO";
    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String AES_MODE = "AES/GCM/NoPadding";

    private static KeyStore mKeyStore;

    static {
        try {
            mKeyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
            mKeyStore.load(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String text_encrypto2text(String str) {

        return "SystemCrypto.text_encrypto2text";
    }

    public static String text_decrypt2text(String str) {
        return "SystemCrypto.text_decrypto2text";
    }
}

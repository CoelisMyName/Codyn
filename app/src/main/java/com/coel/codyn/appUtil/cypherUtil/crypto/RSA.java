package com.coel.codyn.appUtil.cypherUtil.crypto;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;
//加密有117字节限制

public class RSA {
    private static final String ALGORITHM = "RSA/ECB/PKCS1Padding";
    private static final String FACTORY = "RSA";
    private static final int LENGTH = 1024;

    public static byte[] encrypt(PrivateKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static byte[] encrypt(PublicKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static byte[] decrypt(PrivateKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static byte[] decrypt(PublicKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static PrivateKey privateKey(byte[] bytes) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(FACTORY);
        return factory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public static PublicKey publicKey(byte[] bytes) throws Exception {
        KeyFactory keyFactory = KeyFactory.getInstance(FACTORY);
        return keyFactory.generatePublic(new X509EncodedKeySpec(bytes));
    }

    public static KeyPair keyPairGen() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(FACTORY);
        kpg.initialize(LENGTH);
        return kpg.genKeyPair();
    }

    public static byte[] encode(PrivateKey key) {
        return key.getEncoded();
    }

    public static byte[] encode(PublicKey key) {
        return key.getEncoded();
    }

}

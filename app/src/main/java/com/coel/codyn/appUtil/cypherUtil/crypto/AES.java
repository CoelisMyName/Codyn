package com.coel.codyn.appUtil.cypherUtil.crypto;

import org.spongycastle.jcajce.io.CipherInputStream;
import org.spongycastle.jcajce.io.CipherOutputStream;

import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AES {
    private static final String KEYGEN = "AES";
    private static final String ALGORITHM = "AES/CBC/PKCS5PADDING";
    private static final IvParameterSpec IV = new IvParameterSpec(new byte[16]);
    //TODO 文件加密StreamCipher

    public static byte[] encrypt(SecretKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, IV);
        return cipher.doFinal(text);
    }

    public static byte[] encrypt(SecretKey key, byte[] text, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);
        return cipher.doFinal(text);
    }

    public static Cipher getCipher(SecretKey key,int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(opmode, key, IV);
        return cipher;
    }

    public static Cipher getCipher(SecretKey key,int opmode, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(opmode, key, iv);
        return cipher;
    }

    public static byte[] decrypt(SecretKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, IV);
        return cipher.doFinal(text);
    }

    public static byte[] decrypt(SecretKey key, byte[] text, IvParameterSpec iv) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, key, iv);
        return cipher.doFinal(text);
    }

    public static byte[] keyEncode(SecretKey key) {
        return key.getEncoded();
    }

    public static byte[] ivEncode(IvParameterSpec iv) {
        return iv.getIV();
    }

    public static SecretKeySpec keyDecode(byte[] bytes) {
        return new SecretKeySpec(bytes, KEYGEN);
    }

    public static IvParameterSpec ivDecode(byte[] bytes) {
        return new IvParameterSpec(bytes);
    }

    public static SecretKey keyGen() throws Exception {
        KeyGenerator keygen = KeyGenerator.getInstance(KEYGEN);
        keygen.init(256);
        return keygen.generateKey();
    }

    public static IvParameterSpec ivGen() {
        byte[] iv = new byte[16];
        new SecureRandom().nextBytes(iv);
        return new IvParameterSpec(iv);
    }
}

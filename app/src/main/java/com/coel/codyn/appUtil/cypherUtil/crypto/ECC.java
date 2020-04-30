package com.coel.codyn.appUtil.cypherUtil.crypto;

import org.spongycastle.jce.provider.BouncyCastleProvider;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.ECGenParameterSpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class ECC {
    private static final String ALGORITHM = "ECIES";
    private static final String FACTORY = "EC";
    private static final String CURVE = "prime192v1";
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static byte[] encrypt(PrivateKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static byte[] encrypt(PublicKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static byte[] decrypt(PrivateKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    public static byte[] decrypt(PublicKey key, byte[] text) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM, PROVIDER);
        cipher.init(Cipher.DECRYPT_MODE, key);
        return cipher.doFinal(text);
    }

    //以下是字节串转成密钥
    public static PrivateKey privateKey(byte[] bytes) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(FACTORY, PROVIDER);
        return factory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    public static PublicKey publicKey(byte[] bytes) throws Exception {
        KeyFactory factory = KeyFactory.getInstance(FACTORY, PROVIDER);
        return factory.generatePublic(new X509EncodedKeySpec((bytes)));
    }

    /*
    //TODO 研发出私钥变公钥的通用办法
        public static PublicKey ECC_public_key(PrivateKey prvky) {
            return null;
        }
    */
    public static KeyPair keyPairGen() throws Exception {
        KeyPairGenerator keygen = KeyPairGenerator.getInstance(ALGORITHM, PROVIDER);
        keygen.initialize(new ECGenParameterSpec(CURVE));
        KeyPair pair = keygen.generateKeyPair();
        return pair;
    }

    public static byte[] encode(PrivateKey key) {
        return key.getEncoded();
    }

    public static byte[] encode(PublicKey key) {
        return key.getEncoded();
    }
}

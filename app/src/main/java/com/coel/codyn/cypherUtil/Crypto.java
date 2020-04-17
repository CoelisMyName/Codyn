package com.coel.codyn.cypherUtil;

import android.util.Log;

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
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/*
所有用到的加密函数都在这
目前加密字节串，返回加密后字节串
//TODO 研究加密文件
采用椭圆曲线prime192v1
 */

public class Crypto {
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    private static final String ECC_ALGORITHM = "ECIES";//ECIES ECDSA
    private static final String ECC_FACTORY = "EC";
    private static final String ECC_CURVE = "prime192v1";
    private static final String CLASS_NAME = Crypto.class.getName();
    private static final String AES_KEYGEN = "AES";
    private static final String AES_ALGORITHM = "AES/CBC/PKCS5PADDING";

    //向Security加入Provider
    static {
        Security.addProvider(new BouncyCastleProvider());
        //Security.addProvider();
    }

    public static byte[] ECC_encrypt(PrivateKey key, byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(ECC_ALGORITHM, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (Exception e) {
            Log.d(CLASS_NAME, "ECC_encrypt(PrivateKey) ERROR");
        }
        return null;
    }

    public static byte[] ECC_encrypt(PublicKey key, byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(ECC_ALGORITHM, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (Exception e) {
            Log.d(CLASS_NAME, CLASS_NAME + "ECC_encrypt(PublicKey) ERROR");
        }
        return null;
    }

    public static byte[] ECC_decrypt(PrivateKey key, byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(ECC_ALGORITHM, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (Exception e) {
            Log.d(CLASS_NAME, CLASS_NAME + "ECC_decrypt(PrivateKey) ERROR");
        }
        return null;
    }

    public static byte[] ECC_decrypt(PublicKey key, byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(ECC_ALGORITHM, PROVIDER);
            cipher.init(Cipher.DECRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (Exception e) {
            Log.d(CLASS_NAME, CLASS_NAME + "ECC_decrypt(PublicKey) ERROR");
        }
        return null;
    }

    //以下是字节串转成密钥
    public static PrivateKey ECC_private_key(byte[] bytes) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ECC_FACTORY, PROVIDER);
            return factory.generatePrivate(new PKCS8EncodedKeySpec(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(CLASS_NAME, "ECC_private_key(byte[]) ERROR");
        }
        return null;
    }

    public static PublicKey ECC_public_key(byte[] bytes) {
        try {
            KeyFactory factory = KeyFactory.getInstance(ECC_FACTORY, PROVIDER);
            return factory.generatePublic(new X509EncodedKeySpec((bytes)));
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(CLASS_NAME, "ECC_public_key(byte[]) ERROR");
        }
        return null;
    }

    /*
    //TODO 研发出私钥变公钥的通用办法
        public static PublicKey ECC_public_key(PrivateKey prvky) {
            return null;
        }
    */
    public static KeyPair ECC_KeyPairGen() {
        try {
            KeyPairGenerator keygen = KeyPairGenerator.getInstance(ECC_ALGORITHM, PROVIDER);
            Log.d("EC", "pair initial ok");
            keygen.initialize(new ECGenParameterSpec(ECC_CURVE));

            KeyPair pair = keygen.generateKeyPair();
            Log.d(CLASS_NAME, "ECC_KeyPairGen success");
            return pair;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] ECC_private_key2bytes(PrivateKey key) {
        return key.getEncoded();
    }

    public static byte[] ECC_public_key2bytes(PublicKey key) {
        return key.getEncoded();
    }

    public static byte[] AES_encrypt(SecretKey key, byte[] text) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return cipher.doFinal(text);
        } catch (Exception e) {
            Log.d(CLASS_NAME, "AES_encrypt ERROR");
        }
        return null;
    }

    //TODO 文件加密StreamCipher

    /*
    //TODO 需要初始向量
        public static byte[] AES_decrypt(SecretKey key, byte[] text) {
            try {
                Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, key);
                return cipher.doFinal(text);
            }
            catch (Exception e){
                Log.d(CLASS_NAME, "AES_encrypt ERROR");
            }
            return null;
        }
    */
    public static byte[] AES_key2bytes(SecretKey key) {
        return key.getEncoded();
    }

    public static SecretKeySpec AES_key(byte[] bytes) {
        return new SecretKeySpec(bytes, AES_KEYGEN);
    }

    public static SecretKey AES_KeyGen() {
        try {
            KeyGenerator keygen = KeyGenerator.getInstance(AES_KEYGEN);
            keygen.init(256);
            return keygen.generateKey();
        } catch (Exception e) {
            Log.d(CLASS_NAME, "AES_KeyGen ERROR");
        }
        return null;
    }

    public void asda() {
    }
}
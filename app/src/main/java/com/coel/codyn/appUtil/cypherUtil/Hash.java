package com.coel.codyn.appUtil.cypherUtil;

/*
所有用到Hash哈希摘要函数都在这里
返回的都是字节串
 */

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface Hash {

    static byte[] sha256(byte[] bytes) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException ex) {
            Log.d("Hash", "sha256 NoSuchAlgorithmException");
            return null;
        }
        messageDigest.update(bytes);
        return messageDigest.digest();
    }

}

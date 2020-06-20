package com.coel.codyn.fragment.file;

import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.KeyUtil;
import com.coel.codyn.appUtil.cypherUtil.crypto.AES;
import com.coel.codyn.appUtil.cypherUtil.crypto.ECC;
import com.coel.codyn.appUtil.cypherUtil.crypto.RSA;
import com.coel.codyn.room.Key;
import com.coel.codyn.service.FileTask;

import javax.crypto.Cipher;

public class FileTaskBuilder {
    private int type;
    private int attr;
    private byte[] key;
    private String source;
    private String dest;
    private int mode;

    public String getKey() {
        return Coder.Base64_encode2text(key);
    }

    public void setKey(byte[] key) {
        this.key = key;
    }

    public boolean isReady() {
        return key != null && type != -1;
    }

    public String getAttr() {
        return KeyUtil.attrStr(attr);
    }

    public void setAttr(int attr) {
        this.attr = attr;
    }

    public String getMode() {
        switch (mode) {
            case Cipher.ENCRYPT_MODE:
                return "加密";
            case Cipher.DECRYPT_MODE:
                return "解密";
            default:
                return "";
        }
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public String getType() {
        return KeyUtil.type2Str(type);
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void clear() {
        type = -1;
        attr = -1;
        mode = -1;
        key = null;
        source = null;
        dest = null;
    }

    public FileTask build() throws Exception {
        if (key == null || type == -1 || dest == null) {
            return null;
        }
        FileTask fileTask = new FileTask(source, dest, type, attr, key, mode);
        Cipher cipher = null;
        switch (type) {
            case Key.ECC_INT:
                if (attr == Key.PUBLIC_KEY) {
                    cipher = ECC.getCipher(ECC.publicKey(key), mode);
                } else if (attr == Key.PRIVATE_KEY) {
                    cipher = ECC.getCipher(ECC.privateKey(key), mode);
                }
                break;

            case Key.AES_INT:
                if (attr == Key.SYMMETRIC_KEY) {
                    cipher = AES.getCipher(AES.keyDecode(key), mode);
                }
                break;

            case Key.RSA_INT:
                if (attr == Key.PUBLIC_KEY) {
                    cipher = RSA.getCipher(RSA.publicKey(key), mode);
                } else if (attr == Key.PRIVATE_KEY) {
                    cipher = RSA.getCipher(RSA.privateKey(key), mode);
                }
                break;
        }
        if (cipher == null) {
            throw new Exception("cipher is not initialize");
        }
        fileTask.setCipher(cipher);
        return fileTask;
    }
}

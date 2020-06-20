package com.coel.codyn.service;

import org.spongycastle.jcajce.io.CipherOutputStream;

import java.io.IOException;
import java.io.InputStream;

public class StreamCrypto {
    private InputStream is;
    private CipherOutputStream cos;
    private byte[] buffer;
    private int l;

    public void setInputStream(InputStream is) {
        this.is = is;
    }

    public void setCipherOutputStream(CipherOutputStream cos) {
        this.cos = cos;
    }

    public void setBuffer(byte[] buffer) {
        this.buffer = buffer;
    }

    public void clearBuffer() {
        buffer = null;
    }

    int doCrypto() throws IOException {
        l = is.read(buffer);
        if (l > 0) {
            cos.write(buffer, 0, l);
        }
        return l;
    }
}

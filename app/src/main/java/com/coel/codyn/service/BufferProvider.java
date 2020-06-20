package com.coel.codyn.service;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class BufferProvider {
    private final static int MAX_LEN = 2048;
    private final static int MAX_SIZE = 10;
    private static BufferProvider bufferProvider;
    private ArrayList<byte[]> bufferlist = new ArrayList<byte[]>(MAX_SIZE * 2);

    private BufferProvider() {
        for (int i = 0; i < MAX_SIZE; ++i) {
            bufferlist.add(new byte[MAX_LEN]);
        }
        bufferProvider = this;
    }

    public static BufferProvider getInstance() {
        if (bufferProvider == null) {
            synchronized (BufferProvider.class) {
                if (bufferProvider == null) {
                    bufferProvider = new BufferProvider();
                }
            }
        }
        return bufferProvider;
    }

    public synchronized byte[] getBuffer() {
        if (bufferlist.size() > 0)
            return bufferlist.remove(bufferlist.size() - 1);
        return null;
    }

    public synchronized boolean putBuffer(@NonNull byte[] buffer) {
        return bufferlist.add(buffer);
    }
}

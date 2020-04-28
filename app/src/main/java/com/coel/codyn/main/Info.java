package com.coel.codyn.main;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;

public class Info {
    public final static int DEFAULT = -1;
    private final int TYPE;
    private final int ATTR;
    @NonNull
    private byte[] key;

    public Info(int T, int A, @NotNull byte[] k) {
        TYPE = T;
        ATTR = A;
        key = k;
    }

    public int getATTR() {
        return ATTR;
    }

    public int getTYPE() {
        return TYPE;
    }

    @NonNull
    public byte[] getKey() {
        return key;
    }

}

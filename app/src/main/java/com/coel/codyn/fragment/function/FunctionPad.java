package com.coel.codyn.fragment.function;

import androidx.annotation.NonNull;

public class FunctionPad {
    @NonNull
    private static volatile String saveStr = "";
    @NonNull
    private byte[] bin;
    @NonNull
    private String outtxt;

    public FunctionPad() {
        bin = new byte[0];
        outtxt = "";
    }

    @NonNull
    public static String getSaveStr() {
        return saveStr;
    }

    public static void setSaveStr(@NonNull String saveStr) {
        FunctionPad.saveStr = saveStr;
    }

    @NonNull
    public byte[] getBin() {
        return bin;
    }

    public void setBin(@NonNull byte[] bin) {
        this.bin = bin;
    }

    @NonNull
    public String getOuttxt() {
        return outtxt;
    }

    public void setOuttxt(@NonNull String outtxt) {
        this.outtxt = outtxt;
    }
}

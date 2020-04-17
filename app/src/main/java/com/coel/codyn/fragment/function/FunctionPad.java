package com.coel.codyn.fragment.function;

import androidx.annotation.NonNull;

public class FunctionPad {
    @NonNull
    private String outstr;

    public FunctionPad(String s) {
        outstr = s;
    }

    public FunctionPad() {
        outstr = "";
    }

    public String getOutstr() {
        return outstr;
    }

    public void setOutstr(String s) {
        outstr = s;
    }
}

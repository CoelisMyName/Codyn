package com.coel.codyn.service;

import android.widget.TextView;

public interface FileCryptoView extends TaskView {
    public String getSource();

    public String getDest();

    public byte[] getKey();

    public int getMode();

    public int getType();

    public int getAttr();
}

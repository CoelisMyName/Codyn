package com.coel.codyn.service;

public interface FileCryptoView extends TaskView {
    int getId();

    String getSource();

    String getDest();

    String getKey();

    int getMode();

    int getType();

    int getAttr();
}

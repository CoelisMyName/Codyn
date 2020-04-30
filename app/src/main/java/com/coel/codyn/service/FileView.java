package com.coel.codyn.service;

public interface FileView {

    String getSource();

    String getDest();

    byte[] getKey();

    int getMode();

    int getType();

    int getAttr();

    int getProgress();

    int getStat();
}

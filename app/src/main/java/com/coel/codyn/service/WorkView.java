package com.coel.codyn.service;

public interface WorkView {
    final static int FINISHED = 0;
    final static int CANCELED = -1;
    final static int WORKING = 1;
    final static int WAITING = 2;
    final static int PAUSING = 3;

    String getSource();

    String getDest();

    byte[] getKey();

    int getMode();

    int getType();

    int getAttr();

    int getProgress();

    int getStat();
}

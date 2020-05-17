package com.coel.codyn.service;

public interface TaskView {
    final static int FINISHED = 0;//终点状态
    final static int CANCELED = -1;//终点状态
    final static int ERROR = -2;//终点状态
    final static int WORKING = 1;
    final static int WAITING = 2;//操控状态
    final static int PAUSING = 3;//操控状态

    int getProgress();

    int getStat();
}

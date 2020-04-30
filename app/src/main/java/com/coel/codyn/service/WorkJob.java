package com.coel.codyn.service;

import android.os.Handler;

public abstract class WorkJob implements Runnable, WorkControl, WorkView {
    Handler handler;

    //工作内容
    @Override
    public void run() {

    }

    //暂停工作
    @Override
    public void workPause() {

    }

    //移除工作
    @Override
    public void workRemove() {

    }

    //继续工作
    @Override
    public void workResume() {

    }

    //以下是活动View来源
    @Override
    public String getSource() {
        return null;
    }

    @Override
    public String getDest() {
        return null;
    }

    @Override
    public byte[] getKey() {
        return new byte[0];
    }

    @Override
    public int getMode() {
        return 0;
    }

    @Override
    public int getType() {
        return 0;
    }

    @Override
    public int getAttr() {
        return 0;
    }

    @Override
    public int getProgress() {
        return 0;
    }

    @Override
    public int getStat() {
        return 0;
    }
}

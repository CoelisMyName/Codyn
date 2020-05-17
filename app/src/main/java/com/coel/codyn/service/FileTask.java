package com.coel.codyn.service;

import android.os.Handler;

import androidx.annotation.NonNull;

import java.util.concurrent.atomic.AtomicInteger;

public class FileTask implements Runnable, TaskControl, FileCryptoView {
    public final static int MAX = 10000;

    private AtomicInteger stat = new AtomicInteger(WAITING);
    private int progress = 0;
    @NonNull
    private String source;
    @NonNull
    private String dest;
    private int type = -1;
    private int attr = -1;
    private byte[] key = null;
    private int mode = -1;

    Handler handler;

    public FileTask(String s, String d, int t, int a, byte[] k, int m){
        source = s;
        dest = d;
        type = t;
        attr = a;
        mode = m;
        key = k;
    }

    //工作内容
    @Override
    public void run() {

    }

    //暂停工作
    @Override
    public boolean taskPause() {
        int temp = stat.getAndSet(PAUSING);
        if (temp == ERROR || temp == CANCELED || temp == FINISHED) {
            stat.set(temp);
            return false;
        }
        return true;
    }

    //移除工作
    @Override
    public boolean taskCancel() {
        int temp = stat.getAndSet(CANCELED);
        if (temp == ERROR || temp == FINISHED) {
            stat.set(temp);
            return false;
        }
        return true;
    }

    //继续工作
    @Override
    public boolean taskResume() {
        int temp = stat.getAndSet(WAITING);
        if (temp == ERROR || temp == CANCELED || temp == FINISHED) {
            stat.set(temp);
            return false;
        }
        return true;
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
        return progress;
    }

    @Override
    public int getStat() {
        return stat.get();
    }
}

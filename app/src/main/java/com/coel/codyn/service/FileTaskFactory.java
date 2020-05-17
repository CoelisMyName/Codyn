package com.coel.codyn.service;

import androidx.annotation.NonNull;

public class FileTaskFactory {
    public static final int ENCRYPT = 1;
    public static final int DECRYPT = 2;

    private String source= null;
    private String dest= null;
    private int type = -1;
    private int attr = -1;
    private byte[] key = null;

    private int mode = -1;

    public FileTaskFactory(){
    }

    public FileTaskFactory from(@NonNull String url){
        source = url;
        return this;
    }

    public FileTaskFactory using(int t, int a, @NonNull byte[] k){
        type = t;
        attr = a;
        key = k;
        return this;
    }

    public FileTaskFactory in(int m){
        mode = m;
        return this;
    }

    public FileTaskFactory to(String d){
        dest = d;
        return this;
    }

    public FileTask build() throws Exception{

        return null;
    }


}

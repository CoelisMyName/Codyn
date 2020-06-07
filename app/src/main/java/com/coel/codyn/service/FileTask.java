package com.coel.codyn.service;

import android.os.Handler;

import androidx.annotation.NonNull;

import org.spongycastle.jcajce.io.CipherOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;

public class FileTask implements TaskControl, FileCryptoView {
    public final static int MAX = 10000;

    private final AtomicInteger stat = new AtomicInteger(WAITING);
    private int progress = 0;
    private final long size;
    private long index = 0;
    @NonNull
    private String source;
    @NonNull
    private String dest;
    private int type = -1;
    private int attr = -1;
    private byte[] key = null;
    private int mode = -1;
    private StreamCrypto streamCrypto = new StreamCrypto();

    Handler handler;

    //为了安全，限制在100M以内吧
    public FileTask(String source, String dest, int type, int attr, byte[] key, int mode) throws Exception {
        this.source = source;
        this.dest = dest;
        this.type = type;
        this.attr = attr;
        this.mode = mode;
        this.key = key;
        File sf = new File(source);
        size =sf.length();
        streamCrypto.setInputStream(new BufferedInputStream(new FileInputStream(sf)));

    }

    public void setCipher( Cipher cipher) throws Exception {
        streamCrypto.setCipherOutputStream(new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(dest)),cipher));
    }


    //工作内容
    @Override
    public void run() {
        int temp;
        byte[] buffer;
        synchronized (stat) {
            temp = stat.getAndSet(WORKING);
            if (temp != WAITING) {
                stat.set(temp);
                return;
            }

            if((buffer = BufferProvider.getInstance().getBuffer()) == null){
                stat.set(ERROR);
                return;
            }
            streamCrypto.setBuffer(buffer);
        }

        while ((temp = getStat()) == WORKING) {
            try {

                index += streamCrypto.doCrypto();
                progress = (int) ((index * MAX )/ size);
                if(progress == MAX){
                    synchronized (stat) {
                        stat.set(FINISHED);
                    }
                    streamCrypto.clearBuffer();
                    if(!BufferProvider.getInstance().putBuffer(buffer))
                        new Exception("can't put buffer into BufferProvider").printStackTrace();
                    return;
                }

            }catch (Exception ex){
                synchronized (stat){
                    stat.set(ERROR);
                }
                streamCrypto.clearBuffer();
                if(!BufferProvider.getInstance().putBuffer(buffer))
                    new Exception("can't put buffer into BufferProvider").printStackTrace();
                return;
            }
        }

    }

    //暂停工作
    //主线程
    @Override
    public boolean taskPause() {
        synchronized (stat) {
            int temp = stat.getAndSet(PAUSING);
            if (temp == ERROR || temp == CANCELED || temp == FINISHED) {
                stat.set(temp);
                return false;
            }
            return true;
        }
    }

    //移除工作
    //主线程
    @Override
    public boolean taskCancel() {
        synchronized (stat) {
            int temp = stat.getAndSet(CANCELED);
            if (temp == ERROR || temp == FINISHED) {
                stat.set(temp);
                return false;
            }
            return true;
        }
    }

    //继续工作
    //主线程
    @Override
    public boolean taskResume() {
        synchronized (stat) {
            int temp = stat.getAndSet(WAITING);
            if (temp == ERROR || temp == CANCELED || temp == FINISHED) {
                stat.set(temp);
                return false;
            }
            return true;
        }
    }

    //以下是活动View来源
    @Override
    public String getSource() {
        return source;
    }

    @Override
    public String getDest() {
        return dest;
    }

    @Override
    public byte[] getKey() {
        return new byte[0];
    }

    @Override
    public int getMode() {
        return mode;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public int getAttr() {
        return attr;
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

package com.coel.codyn.service;

import androidx.annotation.NonNull;

import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.repository.FileTaskRepository;

import org.spongycastle.jcajce.io.CipherOutputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;

public class FileTask implements TaskControl, FileCryptoView {
    public final static int MAX = 10000;
    private final AtomicInteger stat = new AtomicInteger(WAITING);
    private final long size;
    private int id = FileTaskRepository.getID();
    private int progress = 0;
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
    private FileInputStream fis;
    private FileOutputStream fos;
    private CipherOutputStream cos;
    private String sourceName;
    private String destName;

    //为了安全，限制在100M以内吧
    public FileTask(String source, String dest, int type, int attr, byte[] key, int mode) throws Exception {
        this.source = source;
        this.dest = dest;
        this.type = type;
        this.attr = attr;
        this.mode = mode;
        this.key = key;
        File sf = new File(source);
        File desf = new File(dest);
        sourceName = sf.getName();
        destName = desf.getName();
        fis = new FileInputStream(sf);
        fos = new FileOutputStream(desf);
        size = sf.length();
        streamCrypto.setInputStream(new BufferedInputStream(fis));

    }

    public void setCipher(Cipher cipher) throws Exception {
        cos = new CipherOutputStream(new BufferedOutputStream(fos), cipher);
        streamCrypto.setCipherOutputStream(cos);
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

            if ((buffer = BufferProvider.getInstance().getBuffer()) == null) {
                stat.set(ERROR);
                return;
            }
            streamCrypto.setBuffer(buffer);
        }

        while ((temp = getStat()) == WORKING) {
            try {
                index += streamCrypto.doCrypto();
                progress = (int) ((index * MAX) / size);
                if (progress == MAX) {
                    synchronized (stat) {
                        stat.set(FINISHED);
                    }
                }

            } catch (Exception ex) {
                synchronized (stat) {
                    stat.set(ERROR);
                }
            }
        }
        if (temp == CANCELED || temp == ERROR || temp == FINISHED) {
            streamCrypto.clearBuffer();
            if (!BufferProvider.getInstance().putBuffer(buffer)) {
                new Exception("can't put buffer into BufferProvider").printStackTrace();
            }
            try {
                cos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
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

    @Override
    public int getId() {
        return id;
    }

    //以下是活动View来源
    @Override
    public String getSource() {
        return sourceName;
    }

    @Override
    public String getDest() {
        return destName;
    }

    @Override
    public String getKey() {
        return Coder.Base64_encode2text(key);
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

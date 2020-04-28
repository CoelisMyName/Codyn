package com.coel.codyn.appUtil;

import android.os.AsyncTask;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class PauseATask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private final AtomicBoolean mPaused = new AtomicBoolean(false);
    private Object lock = new Object();

    /*
     * pause，resume主线程调用
     * isPaused在doInBackground轮询，若暂停则阻塞
     */

    public void pause() {
        mPaused.set(true);
    }

    public void resume() {
        mPaused.set(false);
        lock.notify();
    }

    protected final boolean isPaused() {
        boolean b = mPaused.get();
        if (b) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return b;
    }
}

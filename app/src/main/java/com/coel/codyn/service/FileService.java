package com.coel.codyn.service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.coel.codyn.appUtil.PauseATask;

import java.util.concurrent.atomic.AtomicBoolean;

public class FileService extends Service {
    public final static int ERROR = -1;
    public final static int SUCCESS = 0;
    public final static int PAUSE = 1;
    public final static int RESUME = 2;
    public final static int CANCEL = 3;

    public final static int MAX = 10000;

    public final static int ENCRYPT = 1;
    public final static int DECRYPT = 2;

    private CryptoBinder binder = new CryptoBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public class CryptoBinder extends Binder {

    }

    public class FileAnotherAtask extends PauseATask<Void, Integer, Integer> {

        @Override
        protected Integer doInBackground(Void... voids) {
            return null;
        }
    }

    private class FileThread extends Thread {
        public final static int MAX = 10000;
        private volatile boolean isPause = false;
        private volatile boolean isCancel = false;
        private volatile int progress = 0;

        public FileThread() {
        }

        public void pauseProgress() {
            isPause = true;
        }

        public void resumeProgress() {
            isPause = false;
            notify();
        }

        public void cancelProgress() {
            isCancel = true;
        }

        @Override
        public void run() {
            while (!isCancel) {

                if (isPause) {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (isCancel)
                            break;
                    }
                }

                //TODO 做事情
            }
        }
    }

    //似乎AsyncTask没有暂停功能
    public class FileAtask extends AsyncTask<Void, Integer, Integer> {


        private final AtomicBoolean mPaused = new AtomicBoolean(false);
        private Object lock = new Object();
        private volatile int progress = 0;


        public FileAtask(int type, int attr, int mode) {

        }

        public void pause() {
            mPaused.set(true);
        }

        public void resume() {
            mPaused.set(false);
            lock.notify();
        }

        public final boolean isPaused() {
            return mPaused.get();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            //TODO 加密初始化

            while (!isCancelled()) {

                if (isPaused()) {
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        if (isCancelled()) {
                            return CANCEL;
                        }
                    }
                }
                //TODO 加密文件

            }

            if (isCancelled()) {
                return CANCEL;
            }
            return SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

    }
}

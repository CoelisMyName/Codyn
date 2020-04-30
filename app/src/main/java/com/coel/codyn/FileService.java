package com.coel.codyn;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.coel.codyn.service.FileView;

public class FileService extends Service {
    public final static int ERROR = -1;
    public final static int SUCCESS = 0;
    public final static int PAUSE = 1;
    public final static int RESUME = 2;
    public final static int CANCEL = 3;

    public final static int MAX = 10000;

    public final static int ENCRYPT = 1;
    public final static int DECRYPT = 2;

    private Handler handler;

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
        //加解密请求
        public boolean request(String source, String dest, int type, int attr, int mode, byte[] key) {
            return false;
        }

        //暂停
        public void pause(FileView view) {

        }

        //继续
        public void resume(FileView view) {

        }

        //移除
        public void remove(FileView view) {

        }
    }
}

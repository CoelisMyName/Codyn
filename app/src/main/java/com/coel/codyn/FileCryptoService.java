package com.coel.codyn;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.coel.codyn.repository.FileTaskRepository;
import com.coel.codyn.service.FileTask;
import com.coel.codyn.service.TaskView;

public class FileCryptoService extends Service {
    private Handler handler;
    private FileTaskRepository repository;
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
        public void submit(FileTask ft, int r) {
            if(repository == null){
                repository = new FileTaskRepository();
            }
            repository.submit(ft,r);
        }
    }
}

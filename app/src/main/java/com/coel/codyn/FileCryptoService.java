package com.coel.codyn;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.coel.codyn.repository.FileTaskRepository;
import com.coel.codyn.service.FileCryptoView;
import com.coel.codyn.service.FileTask;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FileCryptoService extends Service {
    private Timer timer = new Timer();
    private FileTaskRepository repository;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            repository.invalidate();
            Log.d("refresh", "handleMessage: repository.invalidate()");
            return false;
        }
    });
    private MyTimerTask mTask = new MyTimerTask(handler);
    private CryptoBinder binder = new CryptoBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("service", "onCreate: ");
        repository = FileTaskRepository.getInstance();
        timer.schedule(mTask, 0, 50);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("service", "onDestroy: ");
        timer.cancel();
        mTask.cancel();
        handler.removeCallbacksAndMessages(null);
    }

    public class CryptoBinder extends Binder {
        //加解密请求

        public void submit(FileTask ft, int r) {
            Log.d("task", "submit: task" + r);
            repository.submit(ft, r);
        }

        public LiveData<List<FileCryptoView>> getDisplay() {
            return repository.getDisplay();
        }
    }

    class MyTimerTask extends TimerTask {
        Handler handler;

        public MyTimerTask(Handler handler) {
            this.handler = handler;
        }

        @Override
        public void run() {
            handler.sendEmptyMessage(1);
        }

    }
}

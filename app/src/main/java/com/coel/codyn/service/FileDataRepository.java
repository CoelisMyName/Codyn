package com.coel.codyn.service;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Vector;

public class FileDataRepository {
    private static final int MAX_TASK = 5;
    private static int taskCount = 0;
    private static FileDataRepository fileDataRepository;
    @NonNull
    private List<FileView> list;
    @NonNull
    private MutableLiveData<List<FileView>> listvm;
    @NonNull
    private List<FileView> finishlist;

    private FileDataRepository() {
        listvm = new MutableLiveData<>(new Vector<>(20));
        finishlist = new Vector<>(20);
    }

    private static FileDataRepository getInstance() {
        if (fileDataRepository == null) {
            fileDataRepository = new FileDataRepository();
        }
        return fileDataRepository;
    }

    public int getAllSize() {
        List<FileView> temp = listvm.getValue();
        assert temp != null;
        return temp.size();
    }

    public int getFinishSize() {
        return finishlist.size();
    }

    public boolean append(FileView data) {
        List<FileView> temp = listvm.getValue();
        assert temp != null;
        if (temp.add(data)) {
            listvm.setValue(temp);
        }
        return false;
    }

    public LiveData<List<FileView>> getListvm() {
        return listvm;
    }

    //刷新视图表
    public void refresh() {
        listvm.setValue(listvm.getValue());
    }

    public void done(FileView data) {
        finishlist.add(data);
    }

    private class FileThread extends Thread implements FileView {
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
    }
}

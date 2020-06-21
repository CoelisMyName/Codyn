package com.coel.codyn.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.service.FileCryptoView;
import com.coel.codyn.service.FileTask;

import java.util.List;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FileTaskRepository {
    public static final int START = 1;
    public static final int PAUSE = 2;
    public static final int RESUME = 3;
    public static final int CANCEL = 4;
    public static final int REMOVE = 5;
    private static final int MAX_TASK = 5;
    private static FileTaskRepository repository;
    private static int ID = 0;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_TASK);

    private final List<FileTask> pausingList = new Vector<>(10);
    private final List<FileCryptoView> fileCryptoViews = new Vector<>(10);
    private MutableLiveData<List<FileCryptoView>> display = new MutableLiveData<>();

    public static FileTaskRepository getInstance() {
        if (repository == null)
            repository = new FileTaskRepository();
        return repository;
    }

    public static int getID() {
        return ID++;
    }

    public void invalidate() {
        display.setValue(fileCryptoViews);
    }

    //主线程调用
    public void submit(FileTask f, int s) throws IllegalStateException {
        switch (s) {

            case START:
                if (!fileCryptoViews.contains(f)) {
                    fileCryptoViews.add(f);
                }
                service.submit(f);
                break;

            case PAUSE:
                if (f.taskPause()) {
                    if (!pausingList.contains(f)) {
                        pausingList.add(f);
                    }
                }
                break;

            case RESUME:
                if (f.taskResume()) {
                    pausingList.remove(f);
                    service.submit(f);
                }
                break;

            case CANCEL:
                f.taskCancel();
                break;

            case REMOVE:
                f.taskCancel();
                fileCryptoViews.remove(f);
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
        invalidate();
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        service.shutdown();
    }

    public LiveData<List<FileCryptoView>> getDisplay() {
        return display;
    }
}

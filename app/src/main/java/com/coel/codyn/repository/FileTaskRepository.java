package com.coel.codyn.repository;

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

    private static final int MAX_TASK = 5;
    private static int taskCount = 0;

    private final ExecutorService service = Executors.newFixedThreadPool(MAX_TASK);

    private final List<FileTask> display = new Vector<>(10);
    private final List<FileTask> pausingList = new Vector<>(10);

    //主线程调用
    public void submit(FileTask f, int s) throws IllegalStateException{
        switch (s) {

            case START:
                if(!display.contains(f)){
                    display.add(f);
                }
                service.submit(f);
                break;

            case PAUSE:
                if(f.taskPause()) {
                    if (!pausingList.contains(f)) {
                        pausingList.add(f);
                    }
                }
                break;

            case RESUME:
                if(f.taskResume()) {
                    pausingList.remove(f);
                    service.submit(f);
                }
                break;

            case CANCEL:
                f.taskCancel();
                break;

            default:
                throw new IllegalStateException("Unexpected value: " + s);
        }
    }
}

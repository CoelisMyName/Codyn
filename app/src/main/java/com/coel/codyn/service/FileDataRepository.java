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
    private List<WorkView> list;
    @NonNull
    private MutableLiveData<List<WorkView>> listvm;
    @NonNull
    private List<WorkView> finishlist;

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
        List<WorkView> temp = listvm.getValue();
        assert temp != null;
        return temp.size();
    }

    public int getFinishSize() {
        return finishlist.size();
    }

    public boolean append(WorkView data) {
        List<WorkView> temp = listvm.getValue();
        assert temp != null;
        if (temp.add(data)) {
            listvm.setValue(temp);
        }
        return false;
    }

    public LiveData<List<WorkView>> getListvm() {
        return listvm;
    }

    //刷新视图表
    public void refresh() {
        listvm.setValue(listvm.getValue());
    }

    public void done(WorkView data) {
        finishlist.add(data);
    }

}

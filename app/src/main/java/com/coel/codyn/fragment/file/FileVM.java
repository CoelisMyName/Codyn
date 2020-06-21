package com.coel.codyn.fragment.file;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.coel.codyn.service.FileCryptoView;

import java.util.List;

public class FileVM extends ViewModel {
    private LiveData<List<FileCryptoView>> fcvListLiveData;
    private MutableLiveData<FileTaskBuilder> fileTaskBuilderLiveData;

    public FileVM() {
        super();
        fcvListLiveData = new MutableLiveData<List<FileCryptoView>>();
        fileTaskBuilderLiveData = new MutableLiveData<>();
    }

    public LiveData<List<FileCryptoView>> getFcvListLiveData() {
        return fcvListLiveData;
    }

    public void setFcvListLiveData(LiveData<List<FileCryptoView>> fcvListLiveData) {
        this.fcvListLiveData = fcvListLiveData;
    }

    public LiveData<FileTaskBuilder> getFileTaskBuilderLiveData() {
        return fileTaskBuilderLiveData;
    }

    public void setFileTaskBuilderLiveData(FileTaskBuilder builder) {
        fileTaskBuilderLiveData.setValue(builder);
    }
}

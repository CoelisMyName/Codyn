package com.coel.codyn.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.activitydata.main.Info;
import com.coel.codyn.repository.CodynRepository;
import com.coel.codyn.room.User;

public class MainVM extends AndroidViewModel {
    private int id;
    private LiveData<User> user;
    private MutableLiveData<Info> info;

    private CodynRepository repository;

    public MainVM(@NonNull Application application) {
        super(application);
        repository = new CodynRepository(application);
        info = new MutableLiveData<>(new Info(Info.DEFAULT, Info.DEFAULT, new byte[0]));
    }

    public void initialUser(int id) {
        this.id = id;
        user = repository.find_userLD(id);
    }

    public void updateInfo(Info i) {
        info.setValue(i);
    }

    public LiveData<User> getUser() {
        return user;
    }

    public LiveData<Info> getInfo() {
        return info;
    }

    public void updateUser(User user) {
        repository.updateUser(user);
    }
}

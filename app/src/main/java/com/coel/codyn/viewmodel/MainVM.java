package com.coel.codyn.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.repository.CodynRepository;
import com.coel.codyn.room.User;

public class MainVM extends AndroidViewModel {
    public static final int PUBLIC_KEY = 1;
    public static final int PRIVATE_KEY = 2;
    public static final int SYMMETRIC_KEY = 3;
    private LiveData<User> user;
    private MutableLiveData<Integer> key_type;
    private MutableLiveData<String> key;
    private MutableLiveData<Integer> key_attr;

    private CodynRepository repository;

    public MainVM(@NonNull Application application) {
        super(application);
        repository = new CodynRepository(application);
        key_type = new MutableLiveData<>(-1);
        key = new MutableLiveData<>("");
        user = new MutableLiveData<>();
        key_attr = new MutableLiveData<>(-1);
    }

    public void initialUser(String user_name) {
        user = repository.find_user(user_name);
    }

    public MutableLiveData<Integer> getKey_attr() {
        return key_attr;
    }

    public void setKey_attr(int key_attr) {
        this.key_attr.setValue(key_attr);
    }

    public void updateUser(User user) {
        repository.updateUser(user);
    }

    public MutableLiveData<String> getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key.setValue(key);
    }

    public MutableLiveData<Integer> getKey_type() {
        return key_type;
    }

    public void setKey_type(int key_type) {
        this.key_type.setValue(key_type);
    }

    public LiveData<User> getUser() {
        return user;
    }
}

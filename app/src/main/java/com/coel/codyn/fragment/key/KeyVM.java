package com.coel.codyn.fragment.key;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.repository.CodynRepository;
import com.coel.codyn.room.Key;

import java.util.List;

public class KeyVM extends AndroidViewModel {
    private CodynRepository repository;
    private LiveData<List<Key>> keys;
    private int user_id;

    public KeyVM(Application application) {
        super(application);
        repository = new CodynRepository(application);
        keys = new MutableLiveData<>();//初始化
    }

    public void setUser_id(int i) {
        user_id = i;
        keys = repository.find_keys(user_id);
    }

    public LiveData<List<Key>> getKeys() {
        return keys;
    }

    public void insertKey(Key key) {
        repository.insertKey(key);
    }

    public void deleteKey(Key key) {
        repository.deleteKey(key);
    }

    public void updateKey(Key key) {
        repository.updateKey(key);
    }
}

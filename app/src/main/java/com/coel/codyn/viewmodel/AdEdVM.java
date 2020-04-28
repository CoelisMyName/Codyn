package com.coel.codyn.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.room.Key;

public class AdEdVM extends AndroidViewModel {
    public final static int[] type_list = new int[]{Key.ECC_INT, Key.RSA_INT, Key.AES_INT};
    @NonNull
    private MutableLiveData<Integer> index;

    public AdEdVM(@NonNull Application application) {
        super(application);
        index = new MutableLiveData<>(-1);
    }

    public void nextType() {
        int next = index.getValue() == null ? -1 : (index.getValue() + 1) % type_list.length;
        index.setValue(next);
    }

    public int getType() {
        return (index.getValue() == null || index.getValue() == -1) ? -1 : type_list[index.getValue()];
    }

    public void setType(int type) {
        for (int i = 0; i < type_list.length; ++i) {
            if (type == type_list[i]) {
                index.setValue(i);
            }
        }
    }

    public LiveData<Integer> getTypeLD() {
        return index;
    }

    public void dataChanged(String comment, String pri, String pub) {

    }
}

package com.coel.codyn.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.room.Key;

public class AdEdVM extends AndroidViewModel {
    public final static String KEY_ECC = "ECC";
    public final static String KEY_RSA = "RSA";
    public final static String KEY_AES = "AES";
    private static int[] type_list = new int[]{Key.ECC, Key.RSA, Key.AES};
    private MutableLiveData<Integer> type;
    private int index = -1;

    public AdEdVM(@NonNull Application application) {
        super(application);
        type = new MutableLiveData<>();
        type.setValue(-1);
    }

    public String key_type_Sting(int i) {
        switch (i) {
            case Key.ECC:
                return KEY_ECC;
            case Key.RSA:
                return KEY_RSA;
            case Key.AES:
                return KEY_AES;
            case -1:
                return "按下改变密钥类型";
        }
        Log.d(this.getClass().toString(), "key_type_Sting not have that type");
        return "";
    }

    public void nextType() {
        index = (index + 1) % type_list.length;
        type.setValue(type_list[index]);
    }

    public int getType() {
        if (type.getValue() == null)
            return -1;
        return type.getValue();
    }

    public void setType(int atype) {
        for (int i = 0; i < type_list.length; ++i) {
            if (type_list[i] == atype) {
                index = i;
                return;
            }
        }
        Log.d("AdEdVM", "setType meet error type");
    }

    public LiveData<Integer> getTypeLD() {
        return type;
    }
}

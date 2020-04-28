package com.coel.codyn.fragment.function;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FunctionVM extends AndroidViewModel {
    private MutableLiveData<FunctionPad> pad;

    public FunctionVM(Application application) {
        super(application);
        pad = new MutableLiveData<>(new FunctionPad());
    }

    public LiveData<FunctionPad> getFunctionPad() {
        return pad;
    }

    public void updateBin(byte[] bin) {
        FunctionPad temp = pad.getValue();
        assert temp != null;
        temp.setBin(bin);
        pad.setValue(temp);
    }

    public void updateOuttxt(String str) {
        FunctionPad temp = pad.getValue();
        assert temp != null;
        temp.setOuttxt(str);
        pad.setValue(temp);
    }

    public void saveStr(String s) {
        FunctionPad.setSaveStr(s);
    }
}

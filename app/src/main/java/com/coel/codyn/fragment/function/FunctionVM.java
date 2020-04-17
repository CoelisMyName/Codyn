package com.coel.codyn.fragment.function;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class FunctionVM extends AndroidViewModel {
    private MutableLiveData<List<FunctionPad>> functionPad;

    public FunctionVM(Application application) {
        super(application);
    }

    public MutableLiveData<List<FunctionPad>> getFunctionPad() {
        return functionPad;
    }

    public void setFunctionPad(List<FunctionPad> functionPad) {
        this.functionPad.setValue(functionPad);
    }
}

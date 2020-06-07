package com.coel.codyn.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.R;
import com.coel.codyn.activitydata.register.RegisterFormState;
import com.coel.codyn.activitydata.register.RegisterInUserView;
import com.coel.codyn.activitydata.register.RegisterResult;
import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.Hash;
import com.coel.codyn.repository.CodynRepository;
import com.coel.codyn.room.User;

public class RegisterVM extends AndroidViewModel {
    private CodynRepository repository;
    private MutableLiveData<RegisterFormState> registerFormState = new MutableLiveData<>();
    private MutableLiveData<RegisterResult> registerResult = new MutableLiveData<>();

    public RegisterVM(@NonNull Application application) {
        super(application);
        repository = new CodynRepository(application);
    }

    public void register(String username, String password, String pwrepeat){
        if(isUserNameValid(username) && isPasswordValid(password) && isPasswordSame(password,pwrepeat)){
            new RegisterTask().execute(username,password);
        }
    }

    public LiveData<RegisterFormState> getRegisterFormState() {
        return registerFormState;
    }

    public LiveData<RegisterResult> getRegisterResult() {
        return registerResult;
    }

    private class RegisterTask extends AsyncTask<String,Void,RegisterResult>{

        @Override
        protected RegisterResult doInBackground(String... strings) {
            String name = strings[0], pw = strings[1];
            User users[] = repository.find_user(name);
            if( users == null || users.length == 0 ){
                repository.insertUser(new User(name,Coder.Base64_encode2text(Hash.sha256(pw.getBytes()))));
                return new RegisterResult(new RegisterInUserView(name,pw));
            }
            return new RegisterResult(R.string.register_fail);
        }

        @Override
        protected void onPostExecute(RegisterResult r) {
            super.onPostExecute(r);
            registerResult.setValue(r);
        }
    }

    public void registerDataChanged(String username, String password, String repeat) {
        if (!isUserNameValid(username)) {
            registerFormState.setValue(new RegisterFormState(R.string.invalid_username, null,null));
        }
        else if (!isPasswordValid(password)) {
            registerFormState.setValue(new RegisterFormState(null, R.string.invalid_password,null));
        }
        else if (!isPasswordSame(password, repeat)) {
            registerFormState.setValue(new RegisterFormState(null, null,R.string.invalid_password_repeat));
        }
        else {
            registerFormState.setValue(new RegisterFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    // A placeholder password validation check
    private boolean isPasswordSame(String password, String passwordRepeat) {
        return password.equals(passwordRepeat);
    }
}

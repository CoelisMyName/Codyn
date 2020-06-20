package com.coel.codyn.viewmodel;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.coel.codyn.R;
import com.coel.codyn.activitydata.login.LoggedInUserView;
import com.coel.codyn.activitydata.login.LoginFormState;
import com.coel.codyn.activitydata.login.LoginResult;
import com.coel.codyn.appUtil.cypherUtil.Coder;
import com.coel.codyn.appUtil.cypherUtil.Hash;
import com.coel.codyn.repository.CodynRepository;
import com.coel.codyn.room.User;

import java.util.List;

public class LoginVM extends AndroidViewModel {
    private CodynRepository repository;
    @Nullable
    private List<User> userList;
    private LiveData<List<User>> usersLD;
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    public LoginVM(@NonNull Application application) {
        super(application);

        repository = new CodynRepository(application);
        usersLD = repository.getAllUsersLD();
        //new Atask().execute();
    }

    public LiveData<List<User>> getUsersLD() {
        return usersLD;
    }

    public void setUserList(@Nullable List<User> userList) {
        this.userList = userList;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {

        if (!isUserNameValid(username) || !isPasswordValid(password)) {
            return;
        }

        String phash = Coder.Base64_encode2text(Hash.sha256(password.getBytes()));

        if (userList == null) {
            loginResult.setValue(new LoginResult(R.string.login_error));
            return;
        }


        for (User user : userList) {
            if (user.getUser_name().equals(username) && user.getPassword_hash().equals(phash)) {
                loginResult.setValue(new LoginResult(new LoggedInUserView(user.getId())));
                return;
            }
        }

        loginResult.setValue(new LoginResult(R.string.login_failed));
        Log.d("LG", "user not match");
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
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

    public class Atask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            userList = repository.getAllUsers();
            return null;
        }
    }
}

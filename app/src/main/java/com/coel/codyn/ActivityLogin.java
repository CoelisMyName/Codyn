package com.coel.codyn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coel.codyn.activitydata.login.LoggedInUserView;
import com.coel.codyn.activitydata.login.LoginFormState;
import com.coel.codyn.activitydata.login.LoginResult;
import com.coel.codyn.room.User;
import com.coel.codyn.viewmodel.LoginVM;

import java.util.List;

public class ActivityLogin extends AppCompatActivity {
    public static final int REGISTER_REQUEST = 1;

    public static final String EXTRA_USER_ID = "com.coel.codyn.EXTRA_USER_ID";

    private LoginVM loginVM;
    private EditText usernameEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginVM = new ViewModelProvider(this).get(LoginVM.class);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        loginVM.getUsersLD().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                //更新
                loginVM.setUserList(users);
            }
        });

        loginVM.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginVM.getLoginResult().observe(this, new Observer<LoginResult>() {
            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    Log.d("LG", "loginResult is null");
                    return;
                }
                loadingProgressBar.setVisibility(View.INVISIBLE);
                if (loginResult.getError() != null) {
                    Log.d("LG", "loginResult is Error");
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    Log.d("LG", "loginResult is Success");
                    updateUiWithUser(loginResult.getSuccess());
                    Intent data = new Intent();
                    data.putExtra(EXTRA_USER_ID, loginResult.getSuccess().getUid());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
                //Complete and destroy login activity once successful
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginVM.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginVM.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString());
                    Log.d("LG", "press button");
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                loginVM.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
                Log.d("LG", "press button");
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
        });
    }

    private void startRegister() {
        startActivityForResult(new Intent(this, ActivityRegister.class), REGISTER_REQUEST);
    }

    private void updateUiWithUser(LoggedInUserView model) {
        String welcome = getString(R.string.welcome);
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTER_REQUEST && resultCode == RESULT_OK &&
                data.hasExtra(ActivityRegister.EXTRA_USER_NAME) && data.hasExtra(ActivityRegister.EXTRA_USER_PASSWORD)) {
            String username = data.getStringExtra(ActivityRegister.EXTRA_USER_NAME), password = data.getStringExtra(ActivityRegister.EXTRA_USER_PASSWORD);
            usernameEditText.setText(username);
            passwordEditText.setText(password);
            loginVM.login(username, password);
            return;

        }
        Log.d("ERR", "requestCode: " + requestCode + " resultCode: " + resultCode);
    }
}

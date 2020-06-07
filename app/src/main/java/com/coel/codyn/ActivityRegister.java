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
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.coel.codyn.activitydata.register.RegisterFormState;
import com.coel.codyn.activitydata.register.RegisterInUserView;
import com.coel.codyn.activitydata.register.RegisterResult;
import com.coel.codyn.viewmodel.RegisterVM;

public class ActivityRegister extends AppCompatActivity {
    public static final String EXTRA_USER_NAME = "com.coel.codyn.EXTRA_USER_NAME";
    public static final String EXTRA_USER_PASSWORD = "com.coel.codyn.EXTRA_USER_PASSWORD";

    private RegisterVM registerVM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registerVM = new ViewModelProvider(this).get(RegisterVM.class);
        setContentView(R.layout.activity_register);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final EditText passwordRepeatEditText = findViewById(R.id.password_repeat);
        final Button registerButton = findViewById(R.id.register);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        registerVM.getRegisterResult().observe(this, new Observer<RegisterResult>() {
            @Override
            public void onChanged(RegisterResult registerResult) {
                if(registerResult == null ){
                    return;
                }
                loadingProgressBar.setVisibility(View.INVISIBLE);
                if (registerResult.getSuccess() != null){
                    RegisterInUserView success = registerResult.getSuccess();
                    Intent data = new Intent();
                    data.putExtra(EXTRA_USER_NAME, success.getName());
                    data.putExtra(EXTRA_USER_PASSWORD, success.getPassword());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                    return;
                }
                if(registerResult.getError() != null){
                    Toast.makeText(getApplicationContext(), registerResult.getError(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        registerVM.getRegisterFormState().observe(this, new Observer<RegisterFormState>() {
            @Override
            public void onChanged(RegisterFormState registerFormState) {
                if(registerFormState == null){
                    return;
                }
                registerButton.setEnabled(registerFormState.isDataValid());
                if (registerFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(registerFormState.getUsernameError()));
                }
                if (registerFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordError()));
                }
                if (registerFormState.getPasswordRepeatError() != null) {
                    passwordEditText.setError(getString(registerFormState.getPasswordRepeatError()));
                }
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
                registerVM.registerDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(), passwordRepeatEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordRepeatEditText.addTextChangedListener(afterTextChangedListener);
        passwordRepeatEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    registerVM.register(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),passwordRepeatEditText.getText().toString());
                    Log.d("LG", "press button");
                }
                return false;
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);
                registerVM.register(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),passwordRepeatEditText.getText().toString());
                Log.d("LG", "press button");
            }
        });
    }

}

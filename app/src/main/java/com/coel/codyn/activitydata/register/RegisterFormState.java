package com.coel.codyn.activitydata.register;

import androidx.annotation.Nullable;

public class RegisterFormState {
    @Nullable
    private Integer usernameError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer passwordRepeatError;
    private boolean isDataValid;

    public RegisterFormState(@Nullable Integer usernameError, @Nullable Integer passwordError, @Nullable Integer passwordRepeatError) {
        this.usernameError = usernameError;
        this.passwordError = passwordError;
        this.passwordRepeatError = passwordRepeatError;
        this.isDataValid = false;
    }

    public RegisterFormState(boolean isDataValid) {
        usernameError = null;
        passwordError = null;
        passwordRepeatError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    public Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    public Integer getPasswordRepeatError() {
        return passwordRepeatError;
    }

    @Nullable
    public Integer getUsernameError() {
        return usernameError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}

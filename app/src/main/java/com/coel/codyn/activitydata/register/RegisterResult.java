package com.coel.codyn.activitydata.register;

import androidx.annotation.Nullable;

public class RegisterResult {
    @Nullable
    private RegisterInUserView success;
    @Nullable
    private Integer error;

    public RegisterResult(@Nullable Integer error) {
        this.error = error;
    }

    public RegisterResult(@Nullable RegisterInUserView success) {
        this.success = success;
    }

    @Nullable
    public RegisterInUserView getSuccess() {
        return success;
    }

    @Nullable
    public Integer getError() {
        return error;
    }
}

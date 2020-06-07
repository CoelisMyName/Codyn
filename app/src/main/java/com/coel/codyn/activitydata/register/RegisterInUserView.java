package com.coel.codyn.activitydata.register;

import androidx.annotation.NonNull;

public class RegisterInUserView {
    @NonNull
    private String name;
    @NonNull
    private String password;

    public RegisterInUserView(String n, String pw) {
        name = n;
        password = pw;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @NonNull
    public String getPassword() {
        return password;
    }
}

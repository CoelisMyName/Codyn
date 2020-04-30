package com.coel.codyn.activitydata.login;

/**
 * Class exposing authenticated user details to the UI.
 */
public class LoggedInUserView {
    private int uid;
    //... other data fields that may be accessible to the UI

    public LoggedInUserView(int uid) {
        this.uid = uid;
    }

    public int getUid() {
        return uid;
    }
}

package com.coel.codyn;

import android.app.Application;

/*
自定义的Application
 */

public class CodynApplication extends Application {
    private static CodynApplication application;

    //单例Application
    public static CodynApplication getApplication() {
        return application;
    }

    //初始化
    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }


}

package com.leather.aptdemo;

import android.app.Application;

public class AptApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyRouter.getInstance().init(this);
    }
}

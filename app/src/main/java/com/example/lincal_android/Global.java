package com.example.lincal_android;

import android.app.Application;

public class Global extends Application {
    @Override
    public void onCreate(){

        super.onCreate();
        Singleton.getInstance().userName = "GlobalUserName";
        Singleton.getInstance().password = "Password";
    }
}

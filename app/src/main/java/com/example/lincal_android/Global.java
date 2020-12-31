package com.example.lincal_android;

import android.app.Application;

import org.json.JSONArray;

import java.util.ArrayList;

public class Global extends Application {
    @Override
    public void onCreate(){

        super.onCreate();
        Singleton.getInstance().userName = "GlobalUserName";
        Singleton.getInstance().password = "Password";
        Singleton.getInstance().ownedCalendar = new JSONArray();
    }
}

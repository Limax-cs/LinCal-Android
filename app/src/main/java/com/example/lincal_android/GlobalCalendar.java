package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class GlobalCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_calendar);
        setTitle(Singleton.getInstance().userName);
    }
}
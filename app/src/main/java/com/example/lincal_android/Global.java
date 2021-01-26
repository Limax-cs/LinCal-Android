package com.example.lincal_android;

import android.app.Application;

import org.json.JSONArray;

import java.util.ArrayList;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: Global
Tipus: Application
Funció: especifica variables globals destinades a totes les activitats
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class Global extends Application {
    @Override
    public void onCreate(){

        super.onCreate();

        //Defineix les variables del Singleton
        Singleton.getInstance().userName = "GlobalUserName";
        Singleton.getInstance().password = "Password";
        Singleton.getInstance().ownedCalendars = new JSONArray();
        Singleton.getInstance().editableCalendars = new JSONArray();
        Singleton.getInstance().nonEditableCalendars = new JSONArray();
        Singleton.getInstance().CalendarList = new ArrayList<>();
        Singleton.getInstance().IPaddress = "62.57.250.39";
    }
}

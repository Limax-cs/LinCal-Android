package com.example.lincal_android;

import org.json.JSONArray;

import java.util.Calendar;
import java.util.List;

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    //Aquí s'han de posar les variables globals que volguem mantenir
    public String userName;
    public String password;
    public JSONArray ownedCalendars;
    public JSONArray editableCalendars;
    public JSONArray nonEditableCalendars;


    public static Singleton getInstance() { return ourInstance;}

    private Singleton(){

    }
}

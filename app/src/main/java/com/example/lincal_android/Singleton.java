package com.example.lincal_android;

public class Singleton {
    private static final Singleton ourInstance = new Singleton();

    //Aquí s'han de posar les variables globals que volguem mantenir
    public String userName;
    public String password;


    public static Singleton getInstance() { return ourInstance;}

    private Singleton(){

    }
}

package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: GlobalCalendar
Tipus: AppCompatActivity
Funció: genera el layout de la llista de dies amb els esdeveniments i tasques.
Crida el DaysAdapter per tal de mostrar els objectes d'un dia
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class GlobalCalendar extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_calendar);
        setTitle(Singleton.getInstance().userName);

        //Connectar-se amb el servidor per cridar la llista d'objectes
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            @Override
            public void run() {

                try{

                    //Fa la petició dels calendaris
                    String query = String.format("http://" + Singleton.getInstance().IPaddress + ":9000/AndroidController/getCalendarList?user=" + Singleton.getInstance().userName);
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

                    //Processa la resposta
                    stream = conn.getInputStream();
                    BufferedReader reader = null;
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line = null;
                    while ((line = reader.readLine()) != null){
                        sb.append(line);
                    }
                    result = sb.toString();


                    Log.i("Result:llista jugadors", result);
                    boolean post = handler.post(new Runnable() {
                        public void run() {

                            if(!result.contains("Error"))
                            {
                                //Processament de la resposta JSON
                                ShowJSONresult(result);
                            }
                            else
                            {
                                //Avís d'algún error en la llista
                                Toast.makeText(getBaseContext(),"Hi hagut algún error en la càrrega de calendaris", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(),"Hi ha un error de connexió amb el servidor", Toast.LENGTH_SHORT).show();
                }

            }
        }).start();
    }

    /*
     Funció ShowJSONresult
     Processa les dades rebudes en format JSON en el moment de crear l'activitat i mostreja els dies d'aquesta setmana
     */

    public void ShowJSONresult(String result)
    {

        try {

            //Separa el resultat en tres llistes de calendaris segons el nivell d'accessibilitat de l'usuari
            JSONObject Calendars = new JSONObject(result);
            Singleton.getInstance().ownedCalendars = Calendars.getJSONArray("ownedCalendars");
            Singleton.getInstance().editableCalendars = Calendars.getJSONArray("editableCalendars");
            Singleton.getInstance().nonEditableCalendars = Calendars.getJSONArray("nonEditableCalendars");

            //Carrega els dies d'una setmana dins d'un RecyclerView
            DayUpdate(new Date().getYear(), new Date().getMonth(), new Date().getDate());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /*
     Funció CreateCalendarLayout
     T'envia a una activitat que et permet crear calendaris
     */

    public void CreateCalendarLayout(View view)
    {
        Intent intent = new Intent(this,NewCalendar.class);
        startActivity(intent);
    }

    /*
     Funció CreateEventLayout
     T'envia a una activitat que et permet crear esdeveniments d'un calendari
     */

    public void CreateEventLayout(View view)
    {
        Intent intent = new Intent(this,NewCalEvent.class);
        startActivity(intent);
    }

    /*
     Funció setDay
     Tria un dia dins del calendari perquè es mostrin els esdeveniments i tasques d'altres dies
     */


    //Obre el diàleg de la selecció de dies en un calendari
    public void setDay(View view)
    {
        showDatePickerDialog();
    }

    //Especifica els paràmetres del diàleg de selecció de dia
    private void showDatePickerDialog()
    {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    //Procesa el resultat després d'haver escollit un dia
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        DayUpdate((year-1900),month,dayOfMonth);
    }

    /*
     Funció setDay
     Et porta a la setmana actual en la llista de dies
     */

    public void setToday(View view)
    {
        DayUpdate(new Date().getYear(), new Date().getMonth(), new Date().getDate());
    }

    /*
     Funció DayUpdate
     Carrega els dies d'una setmana especificada en els paràmetres
     */

    public void DayUpdate(int year, int month, int dayOfMonth)
    {
        Singleton.getInstance().CalendarList.clear();

        RecyclerView daysRecyclerView = findViewById(R.id.GlobalCalendar_RecyclerView);

        List<Day> days = new ArrayList<>();
        days.clear();

        Date daySelected = new Date(year,month,dayOfMonth);

        int daySelectedWeek = daySelected.getDay();
        if(daySelected.getDay() == 0)
            daySelectedWeek = 7;


        for(int i=1-daySelectedWeek; i < (8 - daySelectedWeek); i++)
        {
            Day day = new Day();

            day.dateDay.setTime((long)(daySelected.getTime() + i * 24 * 3600 * 1000));
            days.add(day);
        }
        //Crida la funció que organitza els dies de la llista
        Context context = getApplicationContext();
        final DaysAdapter daysAdapter = new DaysAdapter(days, context);
        daysRecyclerView.setAdapter(daysAdapter);
    }


     /*
     Funció LogOut
     Surt de la sessió
     */
    public void LogOut(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
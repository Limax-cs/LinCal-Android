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
                    String query = String.format("http://" + Singleton.getInstance().IPaddress + ":9000/AndroidController/getCalendarList?user=" + Singleton.getInstance().userName);
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.connect();

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
                                //TODO: mostreig de la llista d'objectes a l'aplicació
                                //textview.setText(result);
                                ShowJSONresult(result);
                            }
                            else
                            {
                                //TODO: mostra error de llista buida
                                //textview.setText("bruh");
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();

                }

            }
        }).start();
    }

    public void ShowJSONresult(String result)
    {

        try {

            JSONObject Calendars = new JSONObject(result);
            Singleton.getInstance().ownedCalendars = Calendars.getJSONArray("ownedCalendars");
            Singleton.getInstance().editableCalendars = Calendars.getJSONArray("editableCalendars");
            Singleton.getInstance().nonEditableCalendars = Calendars.getJSONArray("nonEditableCalendars");
            Singleton.getInstance().CalendarList.clear();

            RecyclerView daysRecyclerView = findViewById(R.id.GlobalCalendar_RecyclerView);

            List<Day> days = new ArrayList<>();
            days.clear();

            Date today = new Date();

            int daySelectedWeek = today.getDay();
            if(today.getDay() == 0)
                daySelectedWeek = 7;

            for(int i=1-daySelectedWeek; i < (8 - daySelectedWeek); i++)
            {
                Day day = new Day();

                day.dateDay.setTime((long)(today.getTime() + i * 24 * 3600 * 1000));
                days.add(day);
            }

            Context context = getApplicationContext();
            final DaysAdapter daysAdapter = new DaysAdapter(days, context);
            daysRecyclerView.setAdapter(daysAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //Botons de la pantalla
    public void CreateCalendarLayout(View view)
    {
        Intent intent = new Intent(this,NewCalendar.class);
        startActivity(intent);
    }

    public void CreateEventLayout(View view)
    {
        Intent intent = new Intent(this,NewCalEvent.class);
        startActivity(intent);
    }

    //Selecciona Dia

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

    public void setDay(View view)
    {
        showDatePickerDialog();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        DayUpdate(year,month,dayOfMonth);

    }

    public void setToday(View view)
    {
        DayUpdate(new Date().getYear(), new Date().getMonth(), new Date().getDate());
    }

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

        Context context = getApplicationContext();
        final DaysAdapter daysAdapter = new DaysAdapter(days, context);
        daysRecyclerView.setAdapter(daysAdapter);
    }


}
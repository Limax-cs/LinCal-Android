package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GlobalCalendar extends AppCompatActivity {

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
                    String query = String.format("http://192.168.1.4:9000/AndroidController/getCalendarList?user=" + Singleton.getInstance().userName);
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
                            TextView textview = (TextView) findViewById(R.id.LinCal_GlobalCalendar_adverts);

                            if(!result.contains("Error"))
                            {
                                //TODO: mostreig de la llista d'objectes a l'aplicació
                                textview.setText(result);
                                ShowJSONresult(result);
                            }
                            else
                            {
                                //TODO: mostra error de llista buida
                                textview.setText("bruh");
                            }
                        }
                    });
                }
                catch (Exception e){
                    e.printStackTrace();
                    TextView textview = (TextView) findViewById(R.id.LinCal_GlobalCalendar_adverts);
                    textview.setText("Error");
                }

            }
        }).start();
    }

    public void ShowJSONresult(String result)
    {

        try {
            Singleton.getInstance().ownedCalendar = new JSONArray(result);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
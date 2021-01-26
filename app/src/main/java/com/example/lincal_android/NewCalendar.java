package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: NewCalendar
Tipus: AppCompatActivity
Funció: genera el layout per reomplir les dades d'un calendari i crear-lo. Aquí
també entra la interacció amb el servidor i tots els objectes necessàris per posar
la informació
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class NewCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_calendar);
    }

    public void CreateCalendar(View view)
    {
        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();


            public void run() {

                try {

                    //Strings
                    EditText name = (EditText) findViewById(R.id.NewCalendar_name_txtbox);
                    EditText description = (EditText) findViewById(R.id.NewCalendar_description_txtbox);
                    CheckBox isPublic = (CheckBox) findViewById(R.id.NewCalendar_isPublic_str);

                    String query = String.format("http://" + Singleton.getInstance().IPaddress + ":9000/AndroidController/CreateCalendar"); //IP Albert:192.168.1.4
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    String params = "user=" + Singleton.getInstance().userName +
                            "&password=" + Singleton.getInstance().password +
                            "&CalName=" + name.getText().toString() +
                            "&description=" + description.getText().toString() +
                            "&isPublic=" + isPublic.isChecked()
                            ;

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params.toString());

                    writer.flush();
                    writer.close();
                    os.close();

                    stream = conn.getInputStream();

                    BufferedReader reader = null;

                    StringBuilder sb = new StringBuilder();

                    reader = new BufferedReader(new InputStreamReader(stream));

                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    result = sb.toString();

                    //Codi correcte
                    Log.i("login_response", result);
                    handler.post(new Runnable() {
                        public void run() {

                            if(result.contains("OK"))
                            {
                                Toast.makeText(getBaseContext(),"CALENDARI CREAT",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getBaseContext(),GlobalCalendar.class);
                                startActivity(intent);
                            }
                            else if (result.contains("EXIST"))
                            {
                                Toast.makeText(getBaseContext(),"El calendari ja existeix",Toast.LENGTH_SHORT).show();
                            }
                            else
                                {
                                    Toast.makeText(getBaseContext(),"L'usuari no existeix o la contrassenya és erronia",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

}


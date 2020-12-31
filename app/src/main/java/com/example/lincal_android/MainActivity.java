package com.example.lincal_android;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;
import android.os.StrictMode;
import android.content.pm.PackageManager;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void LoginApp(View view) {


        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {

                    //Strings
                    EditText user = (EditText) findViewById(R.id.Lincal_login_user_txtbox);
                    EditText password = (EditText) findViewById(R.id.Lincal_login_password_txtbox);

                    String query = String.format("http://192.168.1.4:9000/AndroidController/LogIn"); //IP Albert:192.168.1.4
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();


                    String params = "user=" + user.getText().toString() + "&password=" + password.getText().toString();
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

                    // Mostrar resultat en el quadre de text.
                    // Codi incorrecte
                    TextView n = (TextView) findViewById (R.id.Lincal_login_adverts);
                    n.setText(result);

                    //Codi correcte
                    Log.i("login_response", result);
                    handler.post(new Runnable() {
                        public void run() {
                            TextView n = (TextView) findViewById(R.id.Lincal_login_adverts);
                            n.setText("Threads: " + result);

                            if(result.contains("OK"))
                            {
                                //Desa les dades en el Singleton
                                Singleton.getInstance().userName = user.getText().toString();
                                Singleton.getInstance().password = user.getText().toString();

                                //Accedeix a la següent pantalla
                                Context context = getApplicationContext();
                                Intent intent = new Intent(context,GlobalCalendar.class);
                                startActivity(intent);
                            }
                            else{
                                n.setText("Usuari i/o contrassenya incorrectes");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /*

    public void sayHelloAsynktask(View view) {
        EditText user = (EditText) findViewById(R.id.Lincal_login_user_txtbox);
        EditText password = (EditText) findViewById(R.id.Lincal_login_password_txtbox);
        new HelloMessage(this).execute("http://192.168.1.3:9000/Application/LogIn?user=" + user.getText().toString() + "&password=" + password.getText().toString());
    }

    */

/*
    private class HelloMessage extends AsyncTask<String, Void, String> {
        Context context;
        InputStream stream = null;
        String str = "";
        String result = null;

        private HelloMessage(Context context) {
            this.context = context;
        }

        @Override
        protected String doInBackground(String... urls) {

            try {
                String query = String.format(urls[0]);
                URL url = new URL(query);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("GET");
                conn.setDoInput(true);
                conn.connect();
                stream = conn.getInputStream();

                BufferedReader reader = null;

                StringBuilder sb = new StringBuilder();

                reader = new BufferedReader(new InputStreamReader(stream));

                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                result = sb.toString();


                Log.i("lolaforms1", result);


                return result;

            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String result) {

            TextView n = (TextView) findViewById(R.id.Lincal_login_adverts);
            n.setText("AsynkTask: " + result);

        }
    }
    */
}
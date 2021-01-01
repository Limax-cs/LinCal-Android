package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class SignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
    }
    public void SignUp(View view) {

        TextView n = (TextView) findViewById(R.id.Lincal_signup_warnings);
        n.setText("Funciona?");


        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {

                    //Strings
                    n.setText("1");
                    EditText fullname = (EditText) findViewById(R.id.Lincal_signup_fullname_txtbox);
                    EditText email = (EditText) findViewById(R.id.Lincal_signup_email_txtbox);
                    EditText user = (EditText) findViewById(R.id.Lincal_signup_user_txtbox);
                    EditText password = (EditText) findViewById(R.id.Lincal_signup_password_txtbox);

                    n.setText("2");
                    String query = String.format("http://192.168.1.4:9000/AndroidController/SignUp"); //IP Albert:192.168.1.4
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("GET");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    n.setText("3");
                    String params = "fullname=" + fullname.getText().toString() + "&email=" + email.getText().toString()+ "&user=" + user.getText().toString()+ "&password=" + password.getText().toString();
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params.toString());

                    writer.flush();
                    writer.close();
                    os.close();

                    n.setText("4");

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

                    n.setText(result);
                    n.setText("6");

                    //Codi correcte
                    Log.i("signup_response", result);
                    handler.post(new Runnable() {
                        public void run() {
                            n.setText("Threads: " + result);

                            if(result.contains("OK"))
                            {
                                //Desa les dades en el Singleton
                                Singleton.getInstance().userName = user.getText().toString();
                                Singleton.getInstance().password = password.getText().toString();

                                //Accedeix a la següent pantalla
                                Context context = getApplicationContext();
                                Intent intent = new Intent(context,GlobalCalendar.class);
                                startActivity(intent);
                            }
                            else{
                                n.setText("Usuari o correu electrònic ja exixtents");
                            }
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    n.setText("Error");
                }
            }
        }).start();
    }
}
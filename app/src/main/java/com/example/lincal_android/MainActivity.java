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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: MainActivity
Tipus: AppCompatActivity
Funció: genera el layout de registre de l'usuari i la primer activity en mostrar-se
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /*
     Funció SingUp
     T'encamina a una activity nova per crear un nou usuari
     */

    public void SignUp(View view){
        Intent intent = new Intent(this,SignUp.class);
        startActivity(intent);
    }

    /*
     Funció Login
     Es comunica amb el servidor i determina si l'autenticació és correcte o no
     Mostra els esdeveniments i calendaris a una nova activity
     */

    public void LoginApp(View view) {


        new Thread(new Runnable() {
            InputStream stream = null;
            String str = "";
            String result = null;
            Handler handler = new Handler();

            public void run() {

                try {

                    //Obtenció dels valors dels EditText
                    EditText user = (EditText) findViewById(R.id.Lincal_login_user_txtbox);
                    EditText password = (EditText) findViewById(R.id.Lincal_login_password_txtbox);

                    //Crea la petició
                    String query = String.format("http://" + Singleton.getInstance().IPaddress +":9000/AndroidController/LogIn");
                    URL url = new URL(query);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(10000);
                    conn.setConnectTimeout(15000 /* milliseconds */);
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.connect();

                    //Determina les variables de la petició
                    String params = "user=" + user.getText().toString() + "&password=" + password.getText().toString();
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(params.toString());

                    writer.flush();
                    writer.close();
                    os.close();

                    //Llegeix la resposta de la petició
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
                    TextView n = (TextView) findViewById (R.id.Lincal_login_warnings);
                    n.setText(result);

                    //Codi correcte
                    Log.i("login_response", result);
                    handler.post(new Runnable() {
                        public void run() {
                            TextView n = (TextView) findViewById(R.id.Lincal_login_warnings);
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

}
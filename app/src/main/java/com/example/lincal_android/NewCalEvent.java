package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.lincal_android.Singleton.getInstance;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: NewCalEvent
Tipus: AppCompatActivity
Funció: genera la pantalla interactiva de creació d'un esdeveniment. Es communica amb
el servidor i també amb botons necessaris per afegir la informació
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class NewCalEvent extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    Date startDate = new Date();
    Date endDate = new Date();
    Boolean startDateUpdate = false;
    Boolean endDateUpdate = false;
    TextView startDateTV;
    TextView endDateTV;
    String calName = "";

    //Llista de calendaris
    String popUpCalendars[];
    PopupWindow popupWindowCalendars;
    Button calendarListButtonDropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cal_event);

        startDateTV = (TextView)findViewById(R.id.NewCalEvent_startDate_TimeStr);
        endDateTV = (TextView)findViewById(R.id.NewCalEvent_endDate_TimeStr);
        startDateTV.setText(startDate.getDate() + "/" + (1+startDate.getMonth()) + "/" + (1900 + startDate.getYear()) + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
        endDateTV.setText(endDate.getDate() + "/" + (1+endDate.getMonth()) + "/" + (1900 + endDate.getYear()) + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());

        //Llista de calendaris seleccionables
        List<LinCalendar> CalendarList = Singleton.getInstance().CalendarList;
        List<LinCalendar> linCalendars = new ArrayList<>();
        List<String> calendarNames = new ArrayList<>();

        for(int i=0; i < CalendarList.size(); i++)
        {
            LinCalendar cal = CalendarList.get(i);
            if(cal.owned||cal.editable) {
                linCalendars.add(cal);
                calendarNames.add(cal.calName + "::" + i);
            }
        }

        //Conversió de la llista de calendaris seleccionables a un array simple
        popUpCalendars = new String[calendarNames.size()];
        calendarNames.toArray(popUpCalendars);

        //Incialitzar el menú desplegable
        popupWindowCalendars = popupWindowCalendars();

        //Listener del botó de calendaris
        View.OnClickListener handler = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                switch(v.getId()){
                    case R.id.NewCalEvent_calendar_seleccionable:
                        popupWindowCalendars.showAsDropDown(v,-5,0);
                        break;
                }
            }
        };

        //Botons per la selecció dels calendaris
        calendarListButtonDropDown = (Button) findViewById(R.id.NewCalEvent_calendar_seleccionable);
        calendarListButtonDropDown.setOnClickListener(handler);


    }

    public PopupWindow popupWindowCalendars() {

        //Creació de la finestra emergent
        PopupWindow popupWindow = new PopupWindow(this);

        //Creació de la llista dins la finestra emergent
        ListView listViewCalendars = new ListView(this);

        //Relació entre la finestra emergent i la llista -> menú desplegable
        listViewCalendars.setAdapter(calendarsAdapter(popUpCalendars));

        //Crida la funció de selcció d'un objecte de la llista
        listViewCalendars.setOnItemClickListener(new CalendarDropDownOnItemClickListener());

        //Definició d'aspectes visuals de menú desplegable
        popupWindow.setFocusable(true);
        popupWindow.setWidth(400);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        //Fica la informació de la llista de strings a la la llista que veu l'usuari
        popupWindow.setContentView(listViewCalendars);

        return popupWindow;
    }

    //Definició d'aspectes visuals de cada element del menú desplegable
    private ArrayAdapter<String> calendarsAdapter(String dogsArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                //S'agafa el nom del calendari de la llista de strings (la id no s'utilitza)
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                //i es col·loca a la llista que veu l'usuari
                TextView listItem = new TextView(NewCalEvent.this);

                listItem.setText(text);
                listItem.setTag(id);
                listItem.setTextSize(22);
                listItem.setPadding(10, 10, 10, 10);
                listItem.setTextColor(Color.WHITE);

                return listItem;
            }
        };

        return adapter;
    }


    //Finestreta emergent del calendari del sistema per escollir una data concreta
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
    //Finestreta emergent per escollir una hora concreta
    private void showTimePickerDialog()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.HOUR),
                Calendar.getInstance().get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }

    //Recull la data seleccionada i l'escriu en un TextView
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if(startDateUpdate)
        {
            startDate.setYear(year-1900);
            startDate.setMonth(month);
            startDate.setDate(dayOfMonth);

            startDateTV.setText(startDate.getDate() + "/" + (1+startDate.getMonth()) + "/" + (1900 + startDate.getYear()) + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
            startDateUpdate = false;
        }

        if(endDateUpdate)
        {
            endDate.setYear(year-1900);
            endDate.setMonth(month);
            endDate.setDate(dayOfMonth);

            endDateTV.setText(endDate.getDate() + "/" + (1+endDate.getMonth()) + "/" + (1900 + endDate.getYear()) + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());
            endDateUpdate = false;
        }
    }

    //Funció de reposta en clicar el botó de la selecció de la data d'inici
    public void setStartDate(View view)
    {
        startDateUpdate = true;
        showDatePickerDialog();
    }

    //Funció de reposta en clicar el botó de la selecció de la data final
    public void setEndDate(View view)
    {
        endDateUpdate = true;
        showDatePickerDialog();
    }

    //Recull l'hora seleccionada i l'escriu en un TextView
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(startDateUpdate)
        {
            startDate.setHours(hourOfDay);
            startDate.setMinutes(minute);

            startDateTV.setText(startDate.getDate() + "/" + (1+startDate.getMonth()) + "/" + (1900 + startDate.getYear()) + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
            startDateUpdate = false;
        }

        if(endDateUpdate)
        {
            endDate.setHours(hourOfDay);
            endDate.setMinutes(minute);

            endDateTV.setText(endDate.getDate() + "/" + (1+endDate.getMonth()) + "/" + (1900 + endDate.getYear()) + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());
            endDateUpdate = false;
        }
    }

    //Funció de reposta en clicar el botó de la selecció de la hora d'inici
    public void setStartTime(View view)
    {
        startDateUpdate = true;
        showTimePickerDialog();
    }

    //Funció de reposta en clicar el botó de la selecció de la hora final
    public void setEndTime(View view)
    {
        endDateUpdate = true;
        showTimePickerDialog();
    }

    //Botó de creació de l'esdeveniment. Es connecta amb el servidor i li lliura la informació.
    public void CreateEvent(View view) {
        if (calName.isEmpty())
        {
            Toast.makeText(view.getContext(),"Has de definir un calendari", Toast.LENGTH_SHORT).show();
        }
        else {
            new Thread(new Runnable() {
                InputStream stream = null;
                String str = "";
                String result = null;
                Handler handler = new Handler();


                public void run() {

                    try {

                        //Strings
                        EditText name = (EditText) findViewById(R.id.NewCalEvent_name_txtbox);
                        EditText description = (EditText) findViewById(R.id.NewCalEvent_description_txtbox);
                        EditText addressPhysical = (EditText) findViewById(R.id.NewCalEvent_addressPhysical_txtbox);
                        EditText addressOnline = (EditText) findViewById(R.id.NewCalEvent_addressOnline_txtbox);

                        //Petició al servidor
                        String query = String.format("http://" + getInstance().IPaddress + ":9000/AndroidController/CreateEvent"); //IP Albert:192.168.1.4
                        URL url = new URL(query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.connect();

                        //Definir variables
                        String params = "user=" + getInstance().userName +
                                "&password=" + getInstance().password +
                                "&name=" + name.getText().toString() +
                                "&description=" + description.getText().toString() +
                                "&startDate=" + startDate.toString() +
                                "&endDate=" + endDate.toString() +
                                "&addressPhysical=" + addressPhysical.getText().toString() +
                                "&addressOnline=" + addressOnline.getText().toString() +
                                "&calName=" + calName;

                        OutputStream os = conn.getOutputStream();
                        BufferedWriter writer = new BufferedWriter(
                                new OutputStreamWriter(os, "UTF-8"));
                        writer.write(params.toString());

                        writer.flush();
                        writer.close();
                        os.close();

                        //Rebuda de la resposta del servidor
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
                                //Avisos conforme la resposta del servidor
                                if (result.contains("OK")) {
                                    Toast.makeText(getBaseContext(), "ESDEVENIMENT CREAT", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getBaseContext(), GlobalCalendar.class);
                                    startActivity(intent);
                                } else if (result.contains("EMPTY-VARIABLES")) {
                                    Toast.makeText(getBaseContext(), "Falten variables, comprova el nom, les dates i el calendari", Toast.LENGTH_SHORT).show();
                                } else if (result.contains("NO-TIME-EVENT")) {
                                    Toast.makeText(getBaseContext(), "No es pot fer esdeveniments que acabin abans que comencin, posa un marge de temps", Toast.LENGTH_LONG).show();
                                }
                                else if (result.contains("MORE-THAN-1DAY")) {
                                    Toast.makeText(getBaseContext(), "L'esdeveniment dura massa temps", Toast.LENGTH_SHORT).show();
                                }
                                else if (result.contains("INCORRECT-FORMAT")) {
                                    Toast.makeText(getBaseContext(), "El format no és correcte", Toast.LENGTH_SHORT).show();
                                }

                                else {
                                    Toast.makeText(getBaseContext(), "L'usuari no existeix o la contrassenya és erronia", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getBaseContext(),"S'ha d'especificar un calendari",Toast.LENGTH_SHORT);
                    }
                }
            }).start();
        }
    }

}


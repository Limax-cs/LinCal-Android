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
    //PopupWindow
    PopupWindow popupWindowCalendars;
    Button calendarListButtonDropDown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_cal_event);

        startDateTV = (TextView)findViewById(R.id.NewCalEvent_startDate_TimeStr);
        endDateTV = (TextView)findViewById(R.id.NewCalEvent_endDate_TimeStr);
        startDateTV.setText(startDate.getDate() + "/" + (1+startDate.getMonth()) + "/" + startDate.getYear() + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
        endDateTV.setText(endDate.getDate() + "/" + (1+endDate.getMonth()) + "/" + endDate.getYear() + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());

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

        //Conversió a un array simple
        popUpCalendars = new String[calendarNames.size()];
        calendarNames.toArray(popUpCalendars);

        //Incialitzar el popup
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

        // Inicialitza el popup
        PopupWindow popupWindow = new PopupWindow(this);

        //
        ListView listViewCalendars = new ListView(this);

        //
        listViewCalendars.setAdapter(calendarsAdapter(popUpCalendars));

        //
        listViewCalendars.setOnItemClickListener(new CalendarDropDownOnItemClickListener());

        // some other visual settings
        popupWindow.setFocusable(true);
        popupWindow.setWidth(250);
        popupWindow.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        //
        popupWindow.setContentView(listViewCalendars);

        return popupWindow;
    }


    private ArrayAdapter<String> calendarsAdapter(String dogsArray[]) {

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dogsArray) {

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                //
                String item = getItem(position);
                String[] itemArr = item.split("::");
                String text = itemArr[0];
                String id = itemArr[1];

                //
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

    //Selectors de dates

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


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

        if(startDateUpdate)
        {
            startDate.setYear(year);
            startDate.setMonth(month);
            startDate.setDate(dayOfMonth);

            startDateTV.setText(startDate.getDate() + "/" + (1+startDate.getMonth()) + "/" + startDate.getYear() + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
            startDateUpdate = false;
        }

        if(endDateUpdate)
        {
            endDate.setYear(year);
            endDate.setMonth(month);
            endDate.setDate(dayOfMonth);

            endDateTV.setText(endDate.getDate() + "/" + (1+endDate.getMonth()) + "/" + endDate.getYear() + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());
            endDateUpdate = false;
        }
    }

    public void setStartDate(View view)
    {
        startDateUpdate = true;
        showDatePickerDialog();
    }

    public void setEndDate(View view)
    {
        endDateUpdate = true;
        showDatePickerDialog();
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        if(startDateUpdate)
        {
            startDate.setHours(hourOfDay);
            startDate.setMinutes(minute);

            startDateTV.setText(startDate.getDate() + "/" + (1+startDate.getMonth()) + "/" + startDate.getYear() + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
            startDateUpdate = false;
        }

        if(endDateUpdate)
        {
            endDate.setHours(hourOfDay);
            endDate.setMinutes(minute);

            endDateTV.setText(endDate.getDate() + "/" + (1+endDate.getMonth()) + "/" + endDate.getYear() + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());
            endDateUpdate = false;
        }
    }

    public void setStartTime(View view)
    {
        startDateUpdate = true;
        showTimePickerDialog();
    }

    public void setEndTime(View view)
    {
        endDateUpdate = true;
        showTimePickerDialog();
    }

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

                        String query = String.format("http://" + getInstance().IPaddress + ":9000/AndroidController/CreateEvent"); //IP Albert:192.168.1.4
                        URL url = new URL(query);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setReadTimeout(10000);
                        conn.setConnectTimeout(15000 /* milliseconds */);
                        conn.setRequestMethod("GET");
                        conn.setDoInput(true);
                        conn.setDoOutput(true);
                        conn.connect();

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


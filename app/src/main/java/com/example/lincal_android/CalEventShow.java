package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: CalEventShow
Tipus: AppCompatActivity
Funció: genera el layout de la informació d'un esdeveniment
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class CalEventShow extends AppCompatActivity {

    Boolean owned;
    Boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_event_show);
        Intent intent = getIntent();

        //Strings rebuts de l'Activity anterior
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String calendar = intent.getStringExtra("calendar");
        Date startDate = new Date(intent.getStringExtra("startDate"));
        Date endDate = new Date(intent.getStringExtra("endDate"));
        String addressPhysical = intent.getStringExtra("addressPhysical");
        String addressOnline = intent.getStringExtra("addressOnline");
        owned = intent.getBooleanExtra("owned", false);
        editable = intent.getBooleanExtra("editable", false);

        //Instanciem els objectes de l'Activity
        TextView nameTV = (TextView) findViewById(R.id.CalEvent_show_name);
        TextView descriptionTV = (TextView) findViewById(R.id.CalEvent_show_description);
        TextView calendarTV = (TextView) findViewById(R.id.CalEvent_show_calendar);
        TextView addressPhysicalTV = (TextView) findViewById(R.id.calEvent_show_physicalAddress);
        TextView addressOnlineTV = (TextView) findViewById(R.id.calEvent_show_onlineAddress);
        TextView startDateTV = (TextView) findViewById(R.id.CalEvent_show_startDate);
        TextView endDateTV = (TextView) findViewById(R.id.CalEvent_show_endDate);

        //Fer que es pugui enllaçar
        addressOnlineTV.setClickable(true);
        addressOnlineTV.setMovementMethod(LinkMovementMethod.getInstance());
        String htmlAddressOnline = "<a href='https://" + addressOnline + "'>" + addressOnline +"</a>";

        //Editem els textos dels objectes de l'activitat
        nameTV.setText(name);
        descriptionTV.setText(description);
        calendarTV.setText(calendar);
        addressPhysicalTV.setText(addressPhysical);
        addressOnlineTV.setText(Html.fromHtml(htmlAddressOnline));

        String[] month =
                {       "Gener",
                        "Febrer",
                        "Març",
                        "Abril",
                        "Maig",
                        "Juny",
                        "Juliol",
                        "Agost",
                        "Setembre",
                        "Octubre",
                        "Novembre",
                        "Decembre"};

        startDateTV.setText("Dia " + startDate.getDate() + " de " + month[startDate.getMonth()] + " del " + (1900 + startDate.getYear()) + ", " + startDate.getHours() + ":" + startDate.getMinutes() + ":" + startDate.getSeconds());
        endDateTV.setText("Dia " + endDate.getDate() + " de " + month[endDate.getMonth()] + " del " + (1900 + endDate.getYear()) + ", " + endDate.getHours() + ":" + endDate.getMinutes() + ":" + endDate.getSeconds());

    }

    //Butons per modificar la tasca (no són visibles)
    public void editEvent(View view)
    {
        if(editable)
        {
            Toast.makeText(view.getContext(),"EDITANT",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(view.getContext(),"No tens per modificar aquest esdeveniment",Toast.LENGTH_SHORT).show();
    }

    public void deleteEvent(View view)
    {
        if(editable)
        {
            Toast.makeText(view.getContext(),"BORRANT",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(view.getContext(),"No tens per borrar aquest esdeveniment",Toast.LENGTH_SHORT).show();
    }

}
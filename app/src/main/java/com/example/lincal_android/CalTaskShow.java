package com.example.lincal_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: CalTaskShow
Tipus: AppCompatActivity
Funció: genera el layout de la informació d'una tasca
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class CalTaskShow extends AppCompatActivity {

    Boolean owned;
    Boolean editable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cal_task_show);

        Intent intent = getIntent();

        //Strings rebuts de l'Activity anterior
        String name = intent.getStringExtra("name");
        String description = intent.getStringExtra("description");
        String calendar = intent.getStringExtra("calendar");
        Date date = new Date(intent.getStringExtra("date"));
        Boolean completed = intent.getBooleanExtra("completed",false);
        owned = intent.getBooleanExtra("owned",false);
        editable = intent.getBooleanExtra("editable",false);


        //Instanciem els objectes de l'Activity
        TextView nameTV = (TextView) findViewById(R.id.CalTask_show_name);
        TextView descriptionTV = (TextView) findViewById(R.id.CalTask_show_description);
        TextView calendarTV = (TextView) findViewById(R.id.CalTask_show_calendar);
        TextView dateTV = (TextView) findViewById(R.id.CalTask_show_Date);
        ImageView completedIV = (ImageView) findViewById(R.id.CalTask_show_taskcompleted);
        ImageButton editBtn = (ImageButton) findViewById(R.id.CalEvent_editButton);
        ImageButton deleteBtn = (ImageButton) findViewById(R.id.CalEvent_deleteButton);
        ImageButton completeTaskBtn = (ImageButton) findViewById(R.id.CalTask_completeTaskButton);

        //Editem els textos dels objectes de l'activitat
        nameTV.setText(name);
        descriptionTV.setText(description);
        calendarTV.setText(calendar);

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

        dateTV.setText("Dia " + date.getDate() + " de " + month[date.getMonth()] + " del " + (1900 + date.getYear()) + ", " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());


        //Editar la icona de completat
        if(completed)
        {
            completedIV.setImageResource(R.drawable.ic_checked);
        }
        else
        {
            completedIV.setImageResource(R.drawable.ic_unchecked);
        }

    }

    //Butons per modificar la tasca (no són visibles)
    public void editTask(View view)
    {
        if(editable)
        {
            Toast.makeText(view.getContext(),"EDITANT",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(view.getContext(),"No tens per modificar aquesta tasca",Toast.LENGTH_SHORT).show();
    }

    public void deleteTask(View view)
    {
        if(editable)
        {
            Toast.makeText(view.getContext(),"BORRANT",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(view.getContext(),"No tens per borrar aquesta tasca",Toast.LENGTH_SHORT).show();
    }

    public void completeTask(View view)
    {
        if(editable)
        {
            Toast.makeText(view.getContext(),"COMPLETANT",Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(view.getContext(),"No tens per completar aquesta tasca",Toast.LENGTH_SHORT).show();
    }
}
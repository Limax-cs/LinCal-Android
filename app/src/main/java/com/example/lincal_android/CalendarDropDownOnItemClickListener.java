package com.example.lincal_android;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: CalendarDropDownItemClickListener
Tipus: AdapterView.OnItemClickListener
Funció: genera la llista de calendaris desplagable en clicar el botó de calendaris de
NewCalEvent. Es tria un calendari del menú desplagable.
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class CalendarDropDownOnItemClickListener implements AdapterView.OnItemClickListener
{
    @Override
    public void onItemClick(AdapterView<?> arg0, View v, int arg2, long arg3)
    {
        Context context = v.getContext();
        NewCalEvent newCalEvent = ((NewCalEvent) context);


        //Animació del tancament del menú desplegable
        Animation fadeInAnimation = AnimationUtils.loadAnimation(v.getContext(), android.R.anim.fade_in);
        fadeInAnimation.setDuration(10);
        v.startAnimation(fadeInAnimation);

        newCalEvent.popupWindowCalendars.dismiss();

        //Canvi del nom del botó al calendari seleccionat obtenint-ne els valors
        String selectedItemText = ((TextView) v).getText().toString();
        newCalEvent.calendarListButtonDropDown.setText(selectedItemText);
        newCalEvent.calName = selectedItemText;

        //Missatge resposta del marge inferior
        Toast.makeText(context, "Has seleccionat " + selectedItemText,Toast.LENGTH_SHORT).show();


    }
}

package com.example.lincal_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: CalEventsAdapter
Tipus: RecyclerView.Adapter
Funció: genera els objectes dels esdeveniments i la seva interacció amb els
usuaris
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class CalEventsAdapter extends RecyclerView.Adapter<CalEventsAdapter.CalEventsViewHolder>{

    private List<CalEvent> events;
    private Context context;

    public CalEventsAdapter(List<CalEvent> events, Context context)
    {
        this.events = events;
        this.context = context;
    }

    //Mostra l'esdeveniment
    @NonNull
    @Override
    public CalEventsAdapter.CalEventsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CalEventsAdapter.CalEventsViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.dayitem_event,
                        parent,
                        false
                )
        );
    }

    //Relaciona l'objecte del CalEvent amb la seva vista corresponent. Tot seguit, t'envia l'activitat CalEventShow.
    @Override
    public void onBindViewHolder(@NonNull CalEventsAdapter.CalEventsViewHolder holder, int position){
        holder.bindCalEvent(events.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean IsLongClick) {


                Intent intent = new Intent(view.getContext(), CalEventShow.class);
                intent.putExtra("name",events.get(position).name);
                intent.putExtra("calendar",events.get(position).calendarName);
                intent.putExtra("startDate",events.get(position).startDate.toString());
                intent.putExtra("endDate",events.get(position).endDate.toString());
                intent.putExtra("description",events.get(position).description);
                intent.putExtra("addressOnline",events.get(position).addressOnline);
                intent.putExtra("addressPhysical",events.get(position).addressPhysical);
                intent.putExtra("owned",events.get(position).owned);
                intent.putExtra("editable",events.get(position).editable);
                ContextCompat.startActivity(view.getContext(),intent,null);

            }
        });
    }

    //Compta el nombre d'esdeveniments
    @Override
    public int getItemCount(){
        return events.size();
    }

    //Relaciona la informació amb la vista
    class CalEventsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        ConstraintLayout layoutEvent;
        TextView name, calendar, StartDate, EndDate;
        private ItemClickListener eventListener;

        //Agafa els objecte de text
        CalEventsViewHolder(@NonNull View itemsView){
            super(itemsView);
            layoutEvent = itemsView.findViewById(R.id.DayItem_EventBox);
            name = itemsView.findViewById(R.id.eventitem_name);
            calendar = itemsView.findViewById(R.id.eventitem_calendar);
            StartDate = itemsView.findViewById(R.id.eventitem_startdate);
            EndDate = itemsView.findViewById(R.id.eventitem_endDate);

            itemsView.setOnClickListener(this);
            itemsView.setOnLongClickListener(this);

        }

        //Defineix un Listener
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.eventListener = itemClickListener;
        }

        //Detecta que s'ha pitjat l'esdeveniment
        @Override
        public void onClick(View v) {

            eventListener.onClick(v,getAdapterPosition(),false);
        }

        ////Detecta que s'ha pitjat l'esdeveniment en lapse de temps llarg
        @Override
        public boolean onLongClick(View v) {
            eventListener.onClick(v,getAdapterPosition(),true);
            return true;
        }

        //Edita el contingut de la vista
        void bindCalEvent(final CalEvent event)
        {
            String[] week =
                    {       "Diumenge",
                            "Dilluns",
                            "Dimarts",
                            "Dimecres",
                            "Dijous",
                            "Divendres",
                            "Dissabte"
                    };

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

            name.setText(event.name);
            calendar.setText(event.calendarName);
            StartDate.setText("Inici-> " + event.startDate.getDate() + "/" + (event.startDate.getMonth() + 1) + ", " + event.startDate.getHours() + ":" + event.startDate.getMinutes());
            EndDate.setText("Final-> " + event.endDate.getDate() + "/" + (event.endDate.getMonth() + 1) + ", " + event.endDate.getHours() + ":" + event.endDate.getMinutes());


        }
    }
}

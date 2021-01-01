package com.example.lincal_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CalEventsAdapter extends RecyclerView.Adapter<CalEventsAdapter.CalEventsViewHolder>  {

    private List<CalEvent> events;

    public CalEventsAdapter(List<CalEvent> events)
    {
        this.events = events;
    }

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

    @Override
    public void onBindViewHolder(@NonNull CalEventsAdapter.CalEventsViewHolder holder, int position){
        holder.bindCalEvent(events.get(position));
    }

    @Override
    public int getItemCount(){
        return events.size();
    }


    class CalEventsViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutEvent;
        TextView name, calendar, StartDate, EndDate;

        CalEventsViewHolder(@NonNull View itemsView){
            super(itemsView);
            layoutEvent = itemsView.findViewById(R.id.DayItem_EventBox);
            name = itemsView.findViewById(R.id.eventitem_name);
            calendar = itemsView.findViewById(R.id.eventitem_calendar);
            StartDate = itemsView.findViewById(R.id.eventitem_startdate);
            EndDate = itemsView.findViewById(R.id.eventitem_endDate);

        }

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
            EndDate.setText("Final-> = " + event.endDate.getDate() + "/" + (event.endDate.getMonth() + 1) + ", " + event.endDate.getHours() + ":" + event.endDate.getMinutes());


        }
    }
}

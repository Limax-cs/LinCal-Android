package com.example.lincal_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DaysViewHolder> {

    private List<Day> days;
    private RecyclerView.RecycledViewPool CalEventViewPool = new RecyclerView.RecycledViewPool();
    private RecyclerView.RecycledViewPool CalTaskViewPool = new RecyclerView.RecycledViewPool();


    public DaysAdapter(List<Day> days)
    {
        this.days = days;
    }

    @NonNull
    @Override
    public DaysViewHolder onCreateViewHolder(@NonNull ViewGroup  parent, int viewType)
    {
        return new DaysViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.globalcalendar_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position){
        holder.bindDay(days.get(position));
    }

    @Override
    public int getItemCount(){
        return days.size();
    }


    class DaysViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutDay;
        TextView dayText;
        RecyclerView recyclerViewEvents, recyclerViewTasks;

        DaysViewHolder(@NonNull View itemsView){
            super(itemsView);
            layoutDay = itemsView.findViewById(R.id.GlobalCalendar_DayBox);
            dayText = itemsView.findViewById(R.id.GlobalCalendar_DayTitle);
            recyclerViewEvents = itemsView.findViewById(R.id.DayItem_RecycleView_events);
            recyclerViewTasks = itemsView.findViewById(R.id.DayItem_RecycleView_tasks);

        }

        void bindDay(final Day day)
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

            dayText.setText(week[day.dateDay.getDay()] + ", " + day.dateDay.getDate() + " de " + month[day.dateDay.getMonth()] + " del " + (1900 + day.dateDay.getYear()));

            //Fem la llista d'esdeveniments i tasques de cada dia
            List<CalEvent> events = new ArrayList<CalEvent>();
            List<CalTask> tasks= new ArrayList<CalTask>();

            //Creem els LayoutManager amb configuracions predeterminades de cada RecyclerView
            LinearLayoutManager CalEventlayoutManager = new LinearLayoutManager(
              recyclerViewEvents.getContext(),
              LinearLayoutManager.VERTICAL,
                      false
            );

            LinearLayoutManager CalTasklayoutManager = new LinearLayoutManager(
                    recyclerViewTasks.getContext(),
                    LinearLayoutManager.VERTICAL,
                    false
            );

            //Array de calendaris
            for(int i=0;i<Singleton.getInstance().ownedCalendar.length(); i++)
            {
                try {
                    JSONObject calendarItem = Singleton.getInstance().ownedCalendar.getJSONObject(i);
                    String Calendar = calendarItem.getString("calName");

                    //Array d'esdeveniments
                    JSONArray eventItemArray = calendarItem.getJSONArray("events");


                    for(int j=0;j<eventItemArray.length(); j++)
                    {
                        JSONObject eventItem = eventItemArray.getJSONObject(j);
                        Date startDate = new Date(eventItem.getString("startDate"));
                        Date endDate = new Date(eventItem.getString("endDate"));

                        if(
                                ((startDate.getYear() == day.dateDay.getYear()) && (startDate.getMonth() == day.dateDay.getMonth()) && (startDate.getDate() == day.dateDay.getDate()) ) ||
                                ((endDate.getYear() == day.dateDay.getYear()) && (endDate.getMonth() == day.dateDay.getMonth()) && (endDate.getDate() == day.dateDay.getDate()) )
                        )
                        {

                            String name = eventItem.getString("name");
                            String description = eventItem.getString("description");
                            String addressPhysical = eventItem.getString("addressPhysical");
                            String addressOnline = eventItem.getString("addressOnline");

                            CalEvent event = new CalEvent(name, description, startDate, endDate, addressPhysical, addressOnline, Calendar);
                            events.add(event);
                        }
                    }

                    //Array de tasques
                    JSONArray taskItemArray = calendarItem.getJSONArray("tasks");
                    for(int j=0;j<taskItemArray.length(); j++)
                    {
                        JSONObject taskItem = taskItemArray.getJSONObject(j);
                        Date date = new Date(taskItem.getString("date"));

                        if((date.getYear() == day.dateDay.getYear()) && (date.getMonth() == day.dateDay.getMonth()) && (date.getDate() == day.dateDay.getDate()) )
                        {

                            String name = taskItem.getString("name");
                            String description = taskItem.getString("description");
                            boolean completed = taskItem.getBoolean("completed");

                            CalTask task = new CalTask(name, description, date, completed, Calendar);
                            tasks.add(task);
                        }
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


            final CalEventsAdapter calEventsAdapter = new CalEventsAdapter(events);
            recyclerViewEvents.setLayoutManager(CalEventlayoutManager);
            recyclerViewEvents.setAdapter(calEventsAdapter);
            recyclerViewEvents.setRecycledViewPool(CalEventViewPool);

            final CalTasksAdapter calTasksAdapter = new CalTasksAdapter(tasks);
            recyclerViewTasks.setLayoutManager(CalTasklayoutManager);
            recyclerViewTasks.setAdapter(calTasksAdapter);
            recyclerViewTasks.setRecycledViewPool(CalTaskViewPool);
        }
    }

}

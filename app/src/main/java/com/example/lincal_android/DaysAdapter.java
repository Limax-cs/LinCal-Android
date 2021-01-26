package com.example.lincal_android;

import android.content.Context;
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

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: DaysAdapter
Tipus: RecyclerView.Adapter
Funció: genera els objectes dels dies en el layout GlobalCalendar i les seves
interaccions amb els objectes de tasques i calendaris. Crida CalTaskAdapter i/o
CalEventsAdapter per generar els objectes de les tasques i esdeveniments
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class DaysAdapter extends RecyclerView.Adapter<DaysAdapter.DaysViewHolder> {

    private List<Day> days;
    private RecyclerView.RecycledViewPool CalEventViewPool = new RecyclerView.RecycledViewPool();
    private RecyclerView.RecycledViewPool CalTaskViewPool = new RecyclerView.RecycledViewPool();
    private Context context;


    public DaysAdapter(List<Day> days, Context context)
    {
        this.days = days;
        this.context = context;
    }

    //Genera la vista del dia
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

    //Relaciona la vista d'un dia amb la informació d'aquell dia
    @Override
    public void onBindViewHolder(@NonNull DaysViewHolder holder, int position){
        holder.bindDay(days.get(position));
    }

    //Compta el nombre de dies que hi ha a la llista de RecyclerView
    @Override
    public int getItemCount(){
        return days.size();
    }

    //Classe de la vista del dia
    class DaysViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutDay;
        TextView dayText;
        RecyclerView recyclerViewEvents, recyclerViewTasks;

        //Relaciona els textos de la llista i els RecyclerViews
        DaysViewHolder(@NonNull View itemsView){
            super(itemsView);
            layoutDay = itemsView.findViewById(R.id.GlobalCalendar_DayBox);
            dayText = itemsView.findViewById(R.id.GlobalCalendar_DayTitle);
            recyclerViewEvents = itemsView.findViewById(R.id.DayItem_RecycleView_events);
            recyclerViewTasks = itemsView.findViewById(R.id.DayItem_RecycleView_tasks);

        }

        //Es fica la informació d'un dia concret
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
            Singleton.getInstance().CalendarList.clear();

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
            setEventsAndTasksCalendars(Singleton.getInstance().ownedCalendars,events,tasks,day,true,true);
            setEventsAndTasksCalendars(Singleton.getInstance().editableCalendars,events,tasks,day,false,true);
            setEventsAndTasksCalendars(Singleton.getInstance().nonEditableCalendars,events,tasks,day,false,false);


            final CalEventsAdapter calEventsAdapter = new CalEventsAdapter(events, context);
            recyclerViewEvents.setLayoutManager(CalEventlayoutManager);
            recyclerViewEvents.setAdapter(calEventsAdapter);
            recyclerViewEvents.setRecycledViewPool(CalEventViewPool);

            final CalTasksAdapter calTasksAdapter = new CalTasksAdapter(tasks, context);
            recyclerViewTasks.setLayoutManager(CalTasklayoutManager);
            recyclerViewTasks.setAdapter(calTasksAdapter);
            recyclerViewTasks.setRecycledViewPool(CalTaskViewPool);
        }
    }

    //Funció per afegir esdeveniments i tasques
    public void setEventsAndTasksCalendars(JSONArray CalendarList, List<CalEvent> events, List<CalTask> tasks, Day day, Boolean owned, Boolean editable)
    {
        for(int i=0;i<CalendarList.length(); i++) {

            try {

                JSONObject calendarItem = CalendarList.getJSONObject(i);
                String Calendar = calendarItem.getString("calName");
                String CalendarDescription = calendarItem.getString("description");
                Boolean CalendarIsPublic = calendarItem.getBoolean("isPublic");
                Date CalendarCreatedAt = new Date(calendarItem.getString("createdAt"));

                LinCalendar calendar = new LinCalendar(Calendar,CalendarDescription,CalendarCreatedAt,CalendarIsPublic,owned,editable);


                //Array d'esdeveniments
                JSONArray eventItemArray = calendarItem.getJSONArray("events");


                for (int j = 0; j < eventItemArray.length(); j++) {
                    JSONObject eventItem = eventItemArray.getJSONObject(j);
                    Date startDate = new Date(eventItem.getString("startDate"));
                    Date endDate = new Date(eventItem.getString("endDate"));

                    if (
                            ((startDate.getYear() == day.dateDay.getYear()) && (startDate.getMonth() == day.dateDay.getMonth()) && (startDate.getDate() == day.dateDay.getDate())) ||
                                    ((endDate.getYear() == day.dateDay.getYear()) && (endDate.getMonth() == day.dateDay.getMonth()) && (endDate.getDate() == day.dateDay.getDate()))
                    ) {

                        String name = eventItem.getString("name");
                        String description = eventItem.getString("description");
                        String addressPhysical = eventItem.getString("addressPhysical");
                        String addressOnline = eventItem.getString("addressOnline");

                        CalEvent event = new CalEvent(name, description, startDate, endDate, addressPhysical, addressOnline, Calendar,owned,editable);
                        events.add(event);
                        calendar.events.add(event);
                    }
                }

                //Array de tasques
                JSONArray taskItemArray = calendarItem.getJSONArray("tasks");
                for (int j = 0; j < taskItemArray.length(); j++) {
                    JSONObject taskItem = taskItemArray.getJSONObject(j);
                    Date date = new Date(taskItem.getString("date"));

                    if ((date.getYear() == day.dateDay.getYear()) && (date.getMonth() == day.dateDay.getMonth()) && (date.getDate() == day.dateDay.getDate())) {

                        String name = taskItem.getString("name");
                        String description = taskItem.getString("description");
                        boolean completed = taskItem.getBoolean("completed");

                        CalTask task = new CalTask(name, description, date, completed, Calendar,owned,editable);
                        tasks.add(task);
                        calendar.tasks.add(task);
                    }
                }

                Singleton.getInstance().CalendarList.add(calendar);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}

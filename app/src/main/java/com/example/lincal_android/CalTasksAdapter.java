package com.example.lincal_android;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CalTasksAdapter extends RecyclerView.Adapter<CalTasksAdapter.CalTasksViewHolder>{

    private List<CalTask> tasks;

    public CalTasksAdapter(List<CalTask> tasks)
    {
        this.tasks = tasks;
    }

    @NonNull
    @Override
    public CalTasksAdapter.CalTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        return new CalTasksAdapter.CalTasksViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.dayitem_task,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull CalTasksAdapter.CalTasksViewHolder holder, int position){
        holder.bindCalTasks(tasks.get(position));
    }

    @Override
    public int getItemCount(){
        return tasks.size();
    }


    class CalTasksViewHolder extends RecyclerView.ViewHolder{

        ConstraintLayout layoutTask;
        TextView name, calendar, date;

        CalTasksViewHolder(@NonNull View itemsView){
            super(itemsView);
            layoutTask = itemsView.findViewById(R.id.DayItem_TaskBox);
            name = itemsView.findViewById(R.id.taskitem_name);
            calendar = itemsView.findViewById(R.id.taskitem_calendar);
            date = itemsView.findViewById(R.id.taskitem_date);
        }

        void bindCalTasks(final CalTask task)
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

            name.setText(task.name);
            calendar.setText(task.calendarName);
            date.setText(task.date.getDate() + "/" + (task.date.getMonth() + 1) + ", " + task.date.getHours() + ":" + task.date.getMinutes());

        }
    }
}

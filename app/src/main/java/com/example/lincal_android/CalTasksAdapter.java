package com.example.lincal_android;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: CalTasksAdapter
Tipus: RecyclerView.Adapter
Funció: genera els objectes de les tasques i la seva interacció amb els usuaris
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class CalTasksAdapter extends RecyclerView.Adapter<CalTasksAdapter.CalTasksViewHolder>{

    private List<CalTask> tasks;
    public Context context;

    public CalTasksAdapter(List<CalTask> tasks, Context context)
    {

        this.tasks = tasks;
        this.context = context;
    }

    //Mostra la tasca
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

    //Relaciona l'objecte del CalTask amb la seva vista corresponent. Tot seguit, t'envia l'activitat CalTaskShow.
    @Override
    public void onBindViewHolder(@NonNull CalTasksAdapter.CalTasksViewHolder holder, int position){
        holder.bindCalTasks(tasks.get(position));

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean IsLongClick) {
                Intent intent = new Intent(view.getContext(), CalTaskShow.class);
                intent.putExtra("name",tasks.get(position).name);
                intent.putExtra("calendar",tasks.get(position).calendarName);
                intent.putExtra("date",tasks.get(position).date.toString());
                intent.putExtra("completed",tasks.get(position).completed);
                intent.putExtra("description",tasks.get(position).description);
                intent.putExtra("owned",tasks.get(position).owned);
                intent.putExtra("editable",tasks.get(position).editable);
                ContextCompat.startActivity(view.getContext(),intent,null);
            }
        });
    }

    //Compta el nimbre de tasques
    @Override
    public int getItemCount(){
        return tasks.size();
    }


    //Relaciona la informació amb la vista
    class CalTasksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        ConstraintLayout layoutTask;
        TextView name, calendar, date;
        private ItemClickListener taskListener;

        //Agafa els objecte de text
        CalTasksViewHolder(@NonNull View itemsView){
            super(itemsView);
            layoutTask = itemsView.findViewById(R.id.DayItem_TaskBox);
            name = itemsView.findViewById(R.id.taskitem_name);
            calendar = itemsView.findViewById(R.id.taskitem_calendar);
            date = itemsView.findViewById(R.id.taskitem_date);

            itemsView.setOnClickListener(this);
            itemsView.setOnLongClickListener(this);
        }

        //Edita el contingut de la vista
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

        //Defineix un Listener
        public void setItemClickListener(ItemClickListener itemClickListener)
        {
            this.taskListener = itemClickListener;
        }

        //Detecta que s'ha pitjat tasca
        @Override
        public void onClick(View v) {

            taskListener.onClick(v,getAdapterPosition(),false);
        }

        //Detecta que s'ha pitjat tasca en lapse de temps llarg
        @Override
        public boolean onLongClick(View v) {
            taskListener.onClick(v,getAdapterPosition(),true);
            return true;
        }

    }
}

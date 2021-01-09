package com.example.lincal_android;

import java.util.Date;

public class CalTask {
    // Constructor
    public CalTask(String taskName, String taskDescription, Date taskDate, boolean taskfinished, String calendarName, Boolean owned, Boolean editable)
    {
        this.name = taskName;
        this.description = taskDescription;
        this.date = taskDate;
        this.completed = taskfinished;
        this.calendarName = calendarName;
        this.owned = owned;
        this.editable = editable;
    }

    // Atributs
    public String name;
    public String description;
    public Date date;
    public boolean completed;
    public String calendarName;
    public boolean owned;
    public boolean editable;

}

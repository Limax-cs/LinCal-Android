package com.example.lincal_android;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/*~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
Classe: LinCalendar
Tipus: class
Funció: Defineix les variables d'un calendari
 ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~*/

public class LinCalendar {

    //Constructor
    public LinCalendar (String calName, String description, Date createdAt, Boolean isPublic, Boolean owned, Boolean editable)
    {
        this.calName = calName;
        this.description = description;
        this.createdAt = createdAt;
        this.isPublic = isPublic;
        this.owned = owned;
        this.editable = editable;
        events = new ArrayList<>();
        tasks = new ArrayList<>();
    }

    //Atributs
    public String calName;
    public String description;
    public Date createdAt;
    public Boolean isPublic;
    public Boolean owned;
    public Boolean editable;

    public List<CalEvent> events;
    public List<CalTask> tasks;
}

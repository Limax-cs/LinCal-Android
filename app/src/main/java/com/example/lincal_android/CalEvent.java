package com.example.lincal_android;

import java.util.Date;

public class CalEvent {

    // Constructor
    public CalEvent( String eventName, String eventDescription, Date starteventDate,
                    Date finishedeventDate, String addressPhysical, String addressOnline, String calendarName, Boolean owned, Boolean editable)
    {
        this.name = eventName;
        this.description = eventDescription;
        this.startDate = starteventDate;
        this.endDate= finishedeventDate;
        this.addressOnline = addressOnline;
        this.addressPhysical = addressPhysical;
        this.calendarName = calendarName;
        this.owned = owned;
        this.editable = editable;
    }

    // Atributs
    public String name;
    public String description;
    public Date startDate;
    public Date endDate;
    public String addressPhysical;
    public String addressOnline;
    public String calendarName;
    public boolean owned;
    public boolean editable;

}

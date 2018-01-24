/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.time;

import java.util.*;
import utility.Utility;
/**
 *
 * @author irit
 */
public class CalendarObject {

    protected int id;
    protected Date begin_date;
    protected Date end_date;
    protected String entry;
    protected String task_entry;
    Calendar cal;
    
    
    public CalendarObject() {
        id = -1;
        begin_date = null;
        end_date = null;
        entry = "";
        task_entry = "";
    }
    
    
    public CalendarObject(CalendarObject obj) {
        this.id = obj.id;
        this.begin_date = new Date(obj.begin_date.getTime() );
        this.end_date = new Date(obj.end_date.getTime() );
        this.entry = obj.entry;
        this.task_entry = obj.task_entry;
    }
    
    
    public CalendarObject(int id, Date begin, Date end, String entry, String task) {
        this.id = id;
        this.begin_date = new Date(begin.getTime() );
        this.end_date = new Date(end.getTime() );
        this.entry = entry;
        this.task_entry = task;
    }
    
    
    
    public String HTML() {
        StringBuilder object = new StringBuilder();
        cal = Calendar.getInstance();
        object.append("<div class=\"time_object\">");
            object.append("<div>");
                cal.setTime(begin_date);
                object.append(Utility.CalendarToTime(cal) );
            object.append("</div>");
            object.append("<div>");
                cal.setTime(end_date);
                object.append(Utility.CalendarToTime(cal) );
            object.append("</div>");
            object.append("<div>");
                object.append(entry);
            object.append("</div>");
            object.append("<div>");
                object.append("Задача: ");
                object.append(task_entry);
            object.append("</div>");
            object.append("<div>");
            object.append("</div>");
        object.append("</div>");
        
        return object.toString();
    }
}

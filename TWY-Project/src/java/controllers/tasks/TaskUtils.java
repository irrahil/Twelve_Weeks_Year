/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.tasks;

import java.sql.ResultSet;
import java.sql.SQLException;
import servlets.additions.*;
import java.util.*;
import utility.*;
/**
 *
 * @author irit
 */
public class TaskUtils {
    
    static Date date = Calendar.getInstance().getTime();
    
    
    public static void updateTime() {
        date = Calendar.getInstance().getTime();
    }
    
    public static String getListOfPurpose(PGConnection pg, UserConfig user) {
        StringBuilder listOfPurposes = new StringBuilder();
        StringBuilder query = new StringBuilder();
        query.append("SELECT purpose_id, purpose_name FROM purposes ");
        query.append("WHERE year_id = ");
        query.append(String.valueOf(user.task().CurrentYear() ) );
        listOfPurposes.append("<select id=\"new_purpose\">");
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            while (res.next() ) {
                listOfPurposes.append("<option value=");
                listOfPurposes.append(res.getString(1) );
                listOfPurposes.append(">");
                listOfPurposes.append(res.getString(2) );
                listOfPurposes.append("</option>");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        listOfPurposes.append("<option></option>");
        listOfPurposes.append("</select>");
        return listOfPurposes.toString();
    }
    
    
    
    public static int getCurrentDay() {
        Calendar currentDate = java.util.Calendar.getInstance();
        currentDate.setTime(date);
        int day = currentDate.get(Calendar.DAY_OF_WEEK);
        switch (day) {
            case Calendar.MONDAY: return 1;
            case Calendar.TUESDAY: return 2;
            case Calendar.WEDNESDAY: return 3;
            case Calendar.THURSDAY: return 4;
            case Calendar.FRIDAY: return 5;
            case Calendar.SATURDAY: return 6;
            case Calendar.SUNDAY: return 7;
            default: return 0;
        }
    }
    
    
    public static Date getCurrentDate() {
        return date;
    }
    
    
    public static Date getBeginDate(PGConnection pg, int year) {
        Calendar date = Calendar.getInstance();
        StringBuilder query = new StringBuilder();
        query.append("SELECT year_start FROM years ");
        query.append("WHERE year_id = ");
        query.append(String.valueOf(year) );
        query.append(";");
         try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            res.next();
            String d = res.getString(1);
            date = Utility.DateToCalendar(d);
        }catch (SQLException e) {
            e.printStackTrace();
        }
         return date.getTime();
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.time;

import java.sql.ResultSet;
import java.sql.SQLException;
import servlets.additions.*;
import java.util.*;
import json.*;
/**
 *
 * @author irit
 */
public class TimeCtrl {
    
    public static String getListOfYears(PGConnection pg, UserConfig user) {
        StringBuilder query = new StringBuilder();
        StringBuilder list = new StringBuilder();
        query.append("SELECT * FROM years ");
        query.append("WHERE user_id = ");
        query.append(user.id() );
        query.append(";");
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            while (res.next() ) {
                list.append("<button onClick=\"getYearCalendar(");
                list.append(String.valueOf(res.getInt(1) ) );
                list.append("); return false;\">");
                list.append(res.getString(3));
                list.append(" - ");
                list.append(res.getString(4) );
                list.append("</button><hr>");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list.toString();
    }
    
    public static ArrayList<CalendarObjectJSON> getCalendar(PGConnection pg, UserConfig user) {
        ArrayList<CalendarObjectJSON> list = new ArrayList();
        Date year;
        Date begin = new Date();
        Date end = new Date();
        CalendarObjectJSON obj;
        Calendar cal;
        StringBuilder query = new StringBuilder();
        //StringBuilder calendar = new StringBuilder();
        
        //calendar.append("Тестовая версия календаря");
        //calendar.append("<table width=900>");
        /*
            Формирование запроса
        */
        query.append("SELECT c.calendar_id, c.begin_time, c.end_time, ");
        query.append("t.task_name, c.entry, w.week_date, c.day, y.year_start ");
        query.append("FROM calendar AS c, tasks AS t, weeks AS w, years AS y ");
        query.append("WHERE c.task = t.task_id ");
        query.append("AND w.week_id = c.week ");
        query.append("AND w.year = ");
        query.append(user.task().CurrentYear() );
        query.append(" AND w.user_id = ");
        query.append(user.id() );
        query.append(" ORDER BY w.week_date ASC, c.day ASC, c.begin_time ASC ");
        query.append(";");
        
        /*
            Обработка результатов запроса
        */
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            while (res.next() ) {
                year = res.getDate(8);
                year.setTime(year.getTime() + res.getLong(6) * (long) 7 * (long) 86400000 );
                year.setTime(year.getTime() + res.getLong(7) * (long) 86400000);
                begin.setTime(year.getTime() );
                Date time = res.getTimestamp(2);
                begin.setTime(begin.getTime() + res.getTime(2).getTime() + (long) 3 * (long) 3600000);
                end.setTime(res.getTime(3).getTime() );
                end.setTime(end.getTime() + year.getTime() + (long) 3 * (long) 3600000 );
                obj = new CalendarObjectJSON(
                        res.getInt(1),
                        begin,
                        end,
                        res.getString(5),
                        res.getString(4)
                );
                list.add(obj);
            }
        }catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        //for (int i=0; i < list.size(); i++) {
        //    calendar.append(list.get(i).HTML() );
        //}
        //calendar.append("</table>");
        
        return list;
    }
}

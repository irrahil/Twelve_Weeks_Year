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
import controllers.journal.*;
/**
 *
 * @author irit
 */
public class TaskCtrl {
   
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
                list.append("<button onClick=\"getYearTasks(");
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
    
    
    public static String getTaskList(PGConnection pg, UserConfig user) {
        StringBuilder query = new StringBuilder();
        StringBuilder list = new StringBuilder();
        int Week = 0;
        int Week_num = 0;
        int Purpose = 0;
        String Week_str = "";
        Calendar cal = Calendar.getInstance();
        boolean oldWeek = false;
        
        list.append("<table width=900>");
        list.append("<tr class=\"task_page_head\"><td>");
        list.append("<div align=center>Цели на год:</div><hr>");
        query.append("SELECT * FROM purposes ");
        query.append("WHERE year_id = ");
        query.append(user.task().CurrentYear() );
        query.append(" AND purpose_finished = false ORDER BY purpose_id ASC;");
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            while (res.next() ) {
                list.append(res.getString(3));
                list.append("<br>");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        list.append("</td></tr>");
        list.append("<tr><td align=center>");
            list.append("<table class=\"task_page_body\"><tr><td width=60>Номер недели</td><td>Задачи</td><td>Управление");
            query.setLength(0);
            query.append("SELECT w.week_id, w.week_date, t.task_id, t.task_name, t.task_day, t.task_finish, t.purpose_id ");
            query.append("FROM weeks AS w, tasks AS t ");
            query.append("WHERE w.week_id = t.week_id ");
            query.append("AND year = ");
            query.append(user.task().CurrentYear() );
            query.append(" UNION SELECT week_id, week_date, null, null, null, null, null ");
            query.append("FROM weeks WHERE year = ");
            query.append(user.task().CurrentYear() );
            query.append(" ORDER BY 1 ASC, 7 ASC, 5 ASC, 6 ASC;");
            boolean newWeek = false;
            int curDay = TaskUtils.getCurrentDay();
            try {
                ResultSet res = pg.SendSelectQuery(query.toString() );
                if (res != null) {
                    while (res.next() ) {
                        if (Week != res.getInt(1) ) {   //Новая неделя
                            if (newWeek) {      //Работает только в случае, когда это не первая неделя в списке
                                list.append("</td><td>");
                                list.append("<button onClick=\"takePage('editWeek', ");
                                list.append(String.valueOf(Week) );
                                list.append("); return false;\">Изменить</button>");
                                if (oldWeek)
                                    list.append(JournalUtils.getResult(pg, user, Week) );
                            }
                            Week = res.getInt(1);
                            Week_num = res.getInt(2);
                            cal.setTime(user.task().YearDate());
                            cal.add(Calendar.WEEK_OF_YEAR, Week_num);
                            if (TaskUtils.getCurrentDate().after(cal.getTime() ) )
                                oldWeek = true;
                            else
                                oldWeek = false;
                            Purpose = res.getInt(7);
                            cal.add(Calendar.WEEK_OF_YEAR, -1);
                            Week_str = Utility.CalendarToDate(cal).substring(0, 5);
                            cal.add(Calendar.WEEK_OF_YEAR, 1);
                            Week_str = Week_str + " - " + Utility.CalendarToDate(cal).substring(0, 5);
                            
                            list.append("</td></tr><tr><td>");
                            list.append(Week_str );
                            list.append("</td><td>");
                            newWeek = true;
                        }
                        if (res.getString(3) != null) {     //Обрабатываем только те записи, которые имеют ID
                            if (Purpose != res.getInt(7) ) {    //Новая цель в неделе
                                if (oldWeek)
                                    list.append(JournalUtils.getResult(pg, user, Week, Purpose) );
                                Purpose = res.getInt(7);
                                list.append("<hr>");
                            }
                            if (!oldWeek) {
                                list.append("<div style=\"");
                                if (res.getBoolean(6) )
                                    list.append("color: lightgrey;");
                                else if (res.getInt(5) < curDay )
                                    list.append("color: red;");
                                else if (res.getInt(5) == curDay )
                                    list.append("color: green; font-weight: bold");
                                else
                                    list.append("color: black;");
                                list.append("\">");
                                list.append(res.getString(5) );
                                list.append(" - ");
                                list.append(res.getString(4));
                                list.append("</div>");
                            }
                        }
                        
                    }
                }
            }catch (SQLException e) {
                e.printStackTrace();
            }
            list.append("</td><td><button onClick=\"takePage('editWeek', ");
            list.append(String.valueOf(Week) );
            list.append("); return false;\">Изменить</button></tr></table>");
        list.append("</td></tr></table>");
        return list.toString();
    }
    
    
    public static void MakeNewYear(PGConnection pg, UserConfig user, ArrayList<String> params) {
        boolean reverse = false;
        StringBuilder query = new StringBuilder();
        query.append("BEGIN;");
        query.append("INSERT INTO years (user_id, year_start, year_end, ds) ");
        query.append("VALUES (");
        query.append(String.valueOf(user.id() ) );
        query.append(", '");
        String begin_date = params.get(0);
        Calendar cal = Utility.DateToCalendar(begin_date);
        cal.add(Calendar.WEEK_OF_YEAR, 12);
        String end_date = Utility.CalendarToDate(cal);
        //TO DO доработать механизм парсинга даты из веб-формы
        //date.set(0, 0, 0);
        query.append(begin_date );
        query.append("', '");
        query.append(end_date);
        query.append("', 1);");
        pg.SendInsertQuery(query.toString() );
        ResultSet res = pg.SendSelectQuery("SELECT year_id FROM years ORDER BY year_id DESC;");
        int year_id = 0;
        try {
            res.next();
            year_id = res.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
            pg.SendInsertQuery("ROLLBACK; ");
        }
        query.setLength(0);
        if (params.get(1).isEmpty()
            &&  params.get(2).isEmpty()
            &&  params.get(3).isEmpty()
                ) {
            pg.SendInsertQuery("ROLLBACK;");
            return;
        }
        query.append("INSERT INTO purposes (year_id, purpose_name, ds) VALUES ");
        int count = 0;
        for (int i = 1; i < 4; i++) {
            if (!params.get(i).isEmpty() )
                count++;
        }
        int ind = 1;
        for (int i=1; i < 4; i++) {
            if (params.get(i).isEmpty() )
                continue;
            query.append("(");
            query.append(String.valueOf(year_id) );
            query.append(", '");
            query.append(params.get(i) );
            query.append("', 1)");
            if (ind < count)
                query.append(", ");
            ind++;
        }
        query.append("; INSERT INTO weeks (year, user_id, week_date, ds) VALUES ");
        for (int i = 1; i <= 12; i++ ) {
            query.append(" (");
            query.append(String.valueOf(year_id) );
            query.append(", ");
            query.append(String.valueOf(user.id() ) );
            query.append(", ");
            query.append(String.valueOf(i) );
            query.append(", 1)");
            if (i != 12)
                query.append(",");
        }
        query.append("; COMMIT;");
        pg.SendInsertQuery(query.toString() );
        
    }


    /*
        Структура params:
    index=0; value="Week_id"
    */
    public static String getEditPage(PGConnection pg, UserConfig user, ArrayList<String> params) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT t.task_id, t.task_name, t.task_day, t.task_finish, t.purpose_id ");
        query.append("FROM tasks AS t ");
        query.append("WHERE t.user_id = ");
        query.append(String.valueOf(user.id() ) );
        query.append(" AND t.week_id = ");
        query.append(params.get(0) );
        query.append(" ORDER BY 4 ASC, 5 ASC, 3 ASC;");
        StringBuilder menu = new StringBuilder();
        menu.append("<table><tr><td colspan=2>Управление задачами на неделю</td></tr>");
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            int day = 1;
            while (res.next() ) {
                menu.append("<tr><td align=right>");
                if (res.getBoolean(4) ) {
                    menu.append("<div style=\"color: lightgrey;\">");
                }
                menu.append("День ");
                menu.append(res.getString(3) );
                menu.append("><input type=text id=taskName_");
                menu.append(String.valueOf(res.getInt(1) ) );
                menu.append(" value=\"");
                menu.append(res.getString(2));
                menu.append("\" size=100><input type=checkbox id=finish_");
                menu.append(String.valueOf(res.getInt(1) ) );
                if (res.getBoolean(4) ) {
                    menu.append(" checked ");
                }
                menu.append(">");
                menu.append("</td><td align=left>");
                menu.append("<button onClick=\"editTask(");
                menu.append(String.valueOf(res.getInt(1) ) );
                menu.append(", ");
                menu.append(params.get(0) );
                menu.append("); return false;\">Изменить</button>");
                if (res.getBoolean(4) ) {
                    menu.append("</div>");
                }
                menu.append("</td></tr>");
                
            }
            menu.append("<tr><td colspan=2>");
            menu.append("<input type=text id=\"new_taskName\" size=60>");
            menu.append("<select id=\"new_day\">");
            for (int i=1; i < 8; i++) {
                menu.append("<option value=");
                menu.append(String.valueOf(i) );
                menu.append(">");
                menu.append(String.valueOf(i) );
                menu.append("</option>");
            }
            menu.append("</select>");
            menu.append(TaskUtils.getListOfPurpose(pg, user) );
            menu.append("<button onClick=\"");
            menu.append("addNewTask(");
            menu.append(params.get(0));
            menu.append("); return false;");
            menu.append("\">Добавить новую задачу</button></td></tr>");
        }catch (SQLException e) {
            e.printStackTrace();
            menu.append("<tr><td>Ошибка при обращении к БД</td></tr>");
        }
        menu.append("</table>");
        return menu.toString();
    }
    
    
    public static boolean MakeNewTask(PGConnection pg, UserConfig user, ArrayList<String> params) {
        StringBuilder query = new StringBuilder();
        query.append("INSERT INTO tasks (task_name, task_day, week_id, user_id, purpose_id, ds) VALUES ");
        query.append("('");
        query.append(params.get(2) );
        query.append("', ");
        query.append(params.get(1) );
        query.append(", ");
        query.append(params.get(0) );
        query.append(", ");
        query.append(String.valueOf(user.id() ) );
        query.append(", ");
        query.append(params.get(3) );
        query.append(", 1);");
        return pg.SendInsertQuery(query.toString() );
    }
    
    
    
    /*
        Структура params:
    index=1; value="task_id"
    index=2; value="task_finish"
    index=3; value="task_name"
    */
    public static void EditTask(PGConnection pg, UserConfig user, ArrayList<String> params) {
        StringBuilder query = new StringBuilder();
        query.append("UPDATE tasks SET ");
        query.append("task_name = '");
        query.append(params.get(3) );
        query.append("', task_finish = ");
        query.append(params.get(2) );
        query.append(" WHERE task_id = ");
        query.append(params.get(1) );
        pg.SendInsertQuery(query.toString() );
    }
}
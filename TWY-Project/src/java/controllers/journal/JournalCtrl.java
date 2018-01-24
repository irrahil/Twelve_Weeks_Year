/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.journal;
import java.sql.ResultSet;
import java.sql.SQLException;
import servlets.additions.*;
import java.util.*;
import utility.*;
import controllers.journal.*;
import controllers.tasks.TaskUtils;
import patterns.HTML.HTMLStandartConstructs;
/**
 *
 * @author irit
 */
public class JournalCtrl {
    
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
                list.append("<button onClick=\"getYearJournal(");
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
    
    
    
    public static String getJournalList(PGConnection pg, UserConfig user) {
        StringBuilder query = new StringBuilder();
        StringBuilder list = new StringBuilder();
        
        list.append("<table width=100%>");
        list.append("<tr>");
        list.append("<td>Записи в журнале</td><td width=80 align=right>Управление</td>");
        list.append("</tr>");
        
        query.append("SELECT  j.week_id, w.week_date, j.entry, j.entry_id ");
        query.append("FROM journal AS j, weeks AS w ");
        query.append("WHERE j.week_id = w.week_id ");
        query.append("AND w.user_id = ");
        query.append(String.valueOf(user.id() ) );
        query.append(" ");
        query.append("AND w.year = ");
        query.append(String.valueOf(user.task().CurrentYear() ) );
        query.append(" UNION ");
        query.append("SELECT  week_id, week_date, '', null ");
        query.append("FROM weeks ");
        query.append("WHERE user_id = ");
        query.append(String.valueOf(user.id() ) );
        query.append(" ");
        query.append("AND year = ");
        query.append(String.valueOf(user.task().CurrentYear() ) );
        query.append(" ");
        query.append("ORDER BY 2 ASC, 4 ASC;");
        
        boolean newWeek = false;
        int Week, Week_num;
        String Week_str;
        Week = Week_num = 0;
        Calendar cal = Calendar.getInstance();
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            if (res != null) {
                while (res.next() ) {
                    if (Week != res.getInt(1) ) {   //Новая неделя
                        Week = res.getInt(1);
                        Week_num = res.getInt(2);
                        cal.setTime(user.task().YearDate());
                        cal.add(Calendar.WEEK_OF_YEAR, Week_num);
                        cal.add(Calendar.WEEK_OF_YEAR, -1);
                        Week_str = Utility.CalendarToDate(cal).substring(0, 5);
                        cal.add(Calendar.WEEK_OF_YEAR, 1);
                        Week_str = Week_str + " - " + Utility.CalendarToDate(cal).substring(0, 5);

                        list.append("<tr class=\"journal_body_tr\"><td align=center>");
                        list.append(Week_str );
                        list.append("</td><td>");
                        list.append("<button onClick=\"takePage('editJournal', ");
                        list.append(String.valueOf(Week) );
                        list.append("); return false;\">Добавить</button>");
                        list.append("</td></tr>");
                        newWeek = true;
                    }
                    list.append("<tr><td colspan=2 style=\"white-space: pre-wrap;\">");
                    list.append(res.getString(3) );
                    list.append("<hr></td></tr>");
                }
            }
        }catch (SQLException e) {
            e.printStackTrace();
            list.append(e.getMessage() );
            list.append("</table>");
            return list.toString();
        }
        list.append("</table>");
        return list.toString();
    }
    
    
    public static String getAddPage(PGConnection pg, UserConfig user, ArrayList<String> params) {
        return HTMLStandartConstructs.getAddJournalPage(params.get(1) );
    }
    
    
    public static boolean MakeNewEntry(PGConnection pg, UserConfig user, ArrayList<String> params) {
        StringBuilder query = new StringBuilder();
        
        query.append("INSERT INTO journal (entry, week_id, ds) ");
        query.append("VALUES ('");
        query.append(params.get(1) );
        query.append("', ");
        query.append(params.get(0) );
        query.append(", ");
        query.append("1");
        query.append(");");
        
        return pg.SendInsertQuery(query.toString() );
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.journal;

import java.util.*;
import servlets.additions.*;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 * @author irit
 */
public class JournalUtils {
    
    static Date date = Calendar.getInstance().getTime();
    
    
    
    public static String getResult(PGConnection pg, UserConfig user, int week_id) {
        return getResult(pg, user, week_id, 0);
    }
    
    
    
    public static String getResult(PGConnection pg, UserConfig user, int week_id, int purpose) {
        StringBuilder result = new StringBuilder();
        StringBuilder query = new StringBuilder();
        
        query.append("SELECT ");
        query.append("(SELECT COUNT(1) FROM tasks WHERE task_finish = true AND week_id = ");
        query.append(String.valueOf(week_id) );
        query.append(" AND user_id = ");
        query.append(String.valueOf(user.id() ) );
        if (purpose != 0) {
            query.append(" AND purpose_id = ");
            query.append( String.valueOf(purpose) );
        }
        query.append("),");
        query.append("(SELECT COUNT(1) FROM tasks WHERE week_id = ");
        query.append(String.valueOf(week_id) );
        query.append(" AND user_id = ");
        query.append(String.valueOf(user.id() ) );
        if (purpose != 0) {
            query.append(" AND purpose_id = ");
            query.append( String.valueOf(purpose) );
        }
        query.append(")");
        if (purpose != 0) {
            query.append(", (SELECT purpose_name FROM purposes WHERE purpose_id = ");
            query.append( String.valueOf(purpose) );
            query.append(")");
        }
        query.append(";");
        
        
        int finished, count;
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            res.next();
            finished = res.getInt(1);
            count = res.getInt(2);
        }catch (SQLException e) {
            e.printStackTrace();
            return "error in SQL!";
        }
        result.append("<div>");
        result.append("Выполнено: ");
        result.append( String.valueOf(finished) );
        result.append("/");
        result.append( String.valueOf(count) );
        result.append(" (");
        result.append( (String.valueOf(finished*100 / count) ) );
        result.append("%)</div>");
        return result.toString();
    }
}

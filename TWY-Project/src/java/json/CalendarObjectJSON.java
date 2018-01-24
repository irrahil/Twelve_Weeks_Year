/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;
import org.json.*;
import controllers.time.*;
import java.util.Date;
/**
 *
 * @author irit
 */
public class CalendarObjectJSON extends CalendarObject {
    
    public CalendarObjectJSON() {
        super();
    }
    
    
    public CalendarObjectJSON(CalendarObject obj) {
        super(obj);
    }
    
    
    public CalendarObjectJSON(int id, Date begin, Date end, String entry, String task) {
        super(id, begin, end, entry, task);
    }
    
    
    public String json() {
        try {
            JSONObject json = new JSONObject();
            json.put("id", super.id );
            json.put("begin_date", super.begin_date);
            json.put("end_date", super.end_date );
            json.put("entry", super.entry);
            json.put("task_entry", super.task_entry);
            return json.toString();
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }
    
}

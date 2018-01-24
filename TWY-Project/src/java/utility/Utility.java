/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utility;
import java.util.ArrayList;
import java.util.Calendar;
/**
 *
 * @author irit
 */
public class Utility {
    
    public static String[] splitString(String str, String p) {
        ArrayList<String> list = new ArrayList();
        int beginIndex = 0;
        int index;
        do {
            index = str.indexOf(p, beginIndex);
            if (index == -1) {
                list.add(str.substring(beginIndex) );
                break;
            }
            list.add(str.substring(beginIndex, index) );
            beginIndex = index + 1;
        } while (true);
        int size = list.size();
        String[] strings;
        if (size != 0) {
            strings =  new String[size];
            for (int i=0; i < size; i++)
                strings[i] = list.get(i);
        }
        else {
            strings = new String[1];
            strings[0] = str;
        }
        return strings;
        
    }
    
    
    public static Calendar DateToCalendar(String date) {
        boolean reverse = false;
        int day, month, year;
        String[] strArr = splitString(date, "-");
        if (strArr.length != 3) {
            reverse = true;
            strArr = Utility.splitString(date, ".");
        }
        Calendar cal = Calendar.getInstance();
        if (!reverse) {
            year = Integer.parseInt(strArr[0]);
            month = Integer.parseInt(strArr[1]);
            day = Integer.parseInt(strArr[2]);
        }
        else {
            year = Integer.parseInt(strArr[2]);
            month = Integer.parseInt(strArr[1]);
            day = Integer.parseInt(strArr[0]);
        }
        month--;
        cal.set(year, month, day);
        return cal;
    }
    
    
    public static String CalendarToDate(Calendar cal) {
        StringBuilder date = new StringBuilder();
        int day, month, year;
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        String d, m;
        d = String.valueOf(day);
        m = String.valueOf(month);
        if (d.length() == 1)
            d = "0" + d;
        if (m.length() == 1)
            m = "0" + m;
        date.append(d );
        date.append(".");
        date.append(m );
        date.append(".");
        date.append(String.valueOf(year) );
        return date.toString();
    }
    
    
    
    public static String CalendarToTime(Calendar cal) {
        StringBuilder time = new StringBuilder();
        int hour, minutes;
        hour = cal.get(Calendar.HOUR_OF_DAY);
        minutes = cal.get(Calendar.MINUTE);
        String h, m;
        h = String.valueOf(hour);
        m = String.valueOf(minutes);
        if (h.length() == 1)
            h = "0" + h;
        if (m.length() == 1)
            m = "0" + m;
        time.append(h);
        time.append(":");
        time.append(m);
        return time.toString();
    }
}

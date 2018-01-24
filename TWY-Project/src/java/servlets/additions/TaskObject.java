/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets.additions;
import java.util.Date;

/**
 *
 * @author irit
 */
public class TaskObject {

    int currentYear;
    Date beginDate;
    
    public TaskObject() {
        currentYear = -1;
        beginDate = new Date();
    }
    
    public void setYear(int index, Date date) {
        currentYear = index;
        beginDate = date;
    }
    public int CurrentYear() {return currentYear; }
    
    public Date YearDate() {return beginDate; }
}

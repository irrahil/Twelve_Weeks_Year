/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.XML;

/**
 *
 * @author irit
 */
public class NewsEntryObject {
    String entry_date;
    String entry_msg;
    
    public NewsEntryObject(String date, String msg) {
        entry_date = date;
        entry_msg = msg;
    }
    
    
    public String getHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<div align=center>");
        html.append(entry_date);
        html.append("</div><div>");
        html.append(entry_msg);
        html.append("</div>");
        return html.toString();
    }
}

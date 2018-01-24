/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers.XML;

import java.util.ArrayList;
/**
 *
 * @author irit
 */
public class FormObject {
    String formName;
    int width;
    int height;
    int fieldCount;
    boolean method; // TRUE если POST, иначе Get
    ArrayList<FieldObject> fieldList;   //Список полей, хранящихся в форме
    
    public FormObject(String name, int w, int h, boolean getpost) {
        formName = name;
        width = w;
        height = h;
        method = getpost;
        fieldCount = 0;
        fieldList = new ArrayList<FieldObject>();
    }
    
    
    public FormObject() {
        formName = "";
        width = 0;
        height = 0;
        method = false;
        fieldCount = 0;
        fieldList = new ArrayList<FieldObject>();
    }
    
    
    public void addField(FieldObject obj) {
        fieldList.add(obj);
        fieldCount++;
    }
    
    
    public String getFormHTML() {
        StringBuilder html = new StringBuilder();
        html.append("<form id=\"");
        html.append(formName);
        html.append("\" method=\"" + (method?"post":"get") + "\">");
        html.append("<table class=\"form_" + formName + 
                    "\" width=" + String.valueOf(width) + 
                    " height=" + String.valueOf(height) + ">");
        
        for (int i=0; i < fieldCount; i++) {
            if (fieldList.get(i).getType().contentEquals("submit") ) {
                html.append("<tr><td colspan=2 align=center>");
                html.append("<input type=" + fieldList.get(i).getType() );
                html.append(" name=\"" + fieldList.get(i).getName() + "\"");
                html.append(" form=\"" + formName + "\"");
                html.append(" value=\"" + fieldList.get(i).getValue() + "\"></td></tr>");
            }
            else {
                html.append("<tr><td align=right>" + fieldList.get(i).getComment() + "</td>");
                html.append("<td align=left><input type=" + fieldList.get(i).getType() );
                html.append(" name=\"" + fieldList.get(i).getName() + "\"");
                html.append(" form=\"" + formName + "\"");
                html.append(" value=\"" + fieldList.get(i).getValue() + "\"></td></tr>");
            }
        }
        html.append("</table></form>");
        return html.toString();
    }
}

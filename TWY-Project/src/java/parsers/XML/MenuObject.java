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
public class MenuObject {
    int fieldCount;
    ArrayList<ButtonObject> buttons;
    
    public MenuObject() {
        fieldCount = 0;
        buttons = new ArrayList<ButtonObject>();
    }
    
    
    public void addButton(ButtonObject obj) {
        buttons.add(obj);
        fieldCount++;
    } 
    
    public int size() { return fieldCount; }
    
    public String button(int index) {return buttons.get(index).getButtonHTML(); }
    
    public String button(String name) {
        for (int i=0; i < fieldCount; i++) {
            if (buttons.get(i).getButtonName().contains(name) )
                return buttons.get(i).getButtonHTML();
        }
        return "";
    }
}

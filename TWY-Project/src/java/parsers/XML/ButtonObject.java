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
public class ButtonObject {
    String buttonName;
    String buttonValue;
    String buttonCmd;
    ArrayList<String> buttonParams;
    
    public ButtonObject(String buttonName, String ButtonValue, String buttonCmd, ArrayList<String> buttonParams) {
        this.buttonName = buttonName;
        this.buttonValue = ButtonValue;
        this.buttonCmd = buttonCmd;
        this.buttonParams = buttonParams;
    }
    
    
    public String getButtonHTML() {
        StringBuilder button = new StringBuilder();
        button.append("<button onCLick=\"");
        button.append(buttonCmd);
        button.append("(");
        for (int i=0; i < buttonParams.size(); i++) {
            button.append(buttonParams.get(i) );
            if (i < buttonParams.size()-1)
                button.append(", ");
        }
        button.append("); return false;\">");
        button.append(buttonValue);
        button.append("</button>");
        return button.toString();
    }
    
    public String getButtonName() {
        return this.buttonName;
    }
}

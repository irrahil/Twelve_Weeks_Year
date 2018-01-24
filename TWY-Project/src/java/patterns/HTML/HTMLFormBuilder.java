/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package patterns.HTML;
import java.util.HashMap;
import parsers.XML.*;
/**
 *
 * @author irit
 */
public class HTMLFormBuilder {
    HashMap<String, Object> params;
    
    public HTMLFormBuilder(HashMap<String, Object> parameters) {
        params = parameters;            //Получаем указатель на глобальные параметры
    }
    
    public String getLoginForm() {
        return ( (FormObject)params.get("form_Login") ).getFormHTML();
    }
    
    
    public String getRegForm() {
        return ( (FormObject)params.get("form_Reg") ).getFormHTML();
    }
    
    
    
    public String getExitForm() {
        return ( (FormObject)params.get("form_Exit") ).getFormHTML();
    }
    
    
    public String getNewYearForm() {
        return ( (FormObject)params.get("form_NewYear") ).getFormHTML();
    }
}

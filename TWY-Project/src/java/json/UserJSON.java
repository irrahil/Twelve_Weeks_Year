/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package json;

import org.json.*;
import servlets.additions.*;
/**
 *
 * @author irit
 */
public class UserJSON extends UserConfig {
    
    public UserJSON() {
        super();
        
    }
    
    public String getUser() {
        try {
            JSONObject json = new JSONObject();
            json.put("name", super.userName() );
            json.put("token", super.token() );
            return json.toString();
        }catch(JSONException e) {
            e.printStackTrace();
        }
        return "error";
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.authority;

import servlets.additions.PGConnection;
import servlets.additions.UserConfig;

/**
 *
 * @author irit
 */
public class APILoginCtrl extends LoginCtrl {
    
    public static void logIn(PGConnection pg, 
                             String login, 
                             String passwd, 
                             UserConfig user,
                             String device_id) {
        int user_id = LoginCtrl.checkUser(pg, login, passwd);
        if (user_id == -1)
            return;
        user.login(login, user_id, device_id);
    }
}

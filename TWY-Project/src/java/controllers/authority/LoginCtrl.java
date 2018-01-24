/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers.authority;


import servlets.additions.*;
import java.sql.*;
/**
 *
 * @author irit
 */
public  class LoginCtrl {
    
    
    public static void logIn(PGConnection pg, 
                             String login, 
                             String passwd, 
                             UserConfig user) {
        int user_id = checkUser(pg, login, passwd);
        if (user_id == -1)
            return;
        user.login(login, user_id);
    }
    
    
    protected static int checkUser(PGConnection pg, 
                             String login, 
                             String passwd) {
        StringBuilder query = new StringBuilder();
        query.append("SELECT * FROM users WHERE username = ");
        query.append("'");
        query.append(login);
        query.append("' ");
        query.append("AND password = ");
        query.append("'");
        query.append(passwd);
        query.append("';");
        try {
            ResultSet res = pg.SendSelectQuery(query.toString() );
            res.last();
            int size = res.getRow();
            if (size == 1) {
                return res.getInt(1);
            }
        }catch (SQLException e) {
            System.out.println("Trying to login with user name " + login + " and pass " + passwd);
            e.printStackTrace();
        }
        return -1;
    }
}

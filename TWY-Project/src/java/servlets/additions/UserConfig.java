/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets.additions;

import java.util.Calendar;
import java.util.Date;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 *
 * @author irit
 */
public class UserConfig {
    String login;
    int id;
    boolean isLogin;
    TaskObject tasks;
    String token;
    
    
    public UserConfig() {
        this.login = "";
        this.token = "";
        isLogin = false;
        tasks = new TaskObject();
    }
    
    
    public boolean isLogin() {return isLogin; }
    public String userName() {return login; }
    public int id() {return id; }
    public void login(String name, int id) {
        login = name; this.id = id; isLogin = true; generateToken(); 
    }
    public void login(String name, int id, String id_device) {
        login = name; this.id = id; isLogin = true; generateToken(id_device);
    }
    public void logout() {login = ""; isLogin = false; }
    public TaskObject task() {return tasks; }
    public String token() {return token; }
    
    private void generateToken() {
        generateToken("");
    }
    private void generateToken(String id_device) {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        //TODO генерация токена на основании логина, пароля, времени входа в систему, идентификатора пользователя
        //и идентификатора устройства.
        token = login + date.toString() + Integer.toString(id) + id_device;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] bytes = md.digest(token.getBytes() );
            StringBuilder tokenizer = new StringBuilder();
            for (int i=0; i < bytes.length; i++) {
                tokenizer.append(Integer.toString( (bytes[i] & 0xff) + 0x100, 16).substring(1) );
            }
            token = tokenizer.toString();
        }
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

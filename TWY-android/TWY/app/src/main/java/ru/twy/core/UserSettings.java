package ru.twy.core;

/**
 * Created by irit on 09.10.16.
 */

public class UserSettings {

    String login;
    String pass;
    String token;

    public UserSettings() {
        login = "";
        token = "";
        pass = "";
    }


    public UserSettings(String name, String pass) {
        login = name;
        token = "";
        this.pass = pass;
    }



    public void setLogin(String name) {
        this.login = name;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setPass(String pass) {
        this.pass = pass;
    }



    public String getLogin() {
        return this.login;
    }
    public String getToken() {
        return this.token;
    }
    public String getPass() {
        return this.pass;
    }
}

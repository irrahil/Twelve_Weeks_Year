package ru.twy.core;


import android.content.Context;
import android.telephony.TelephonyManager;

/**
 * Created by irit on 09.10.16.
 */

public class Settings {

    private static Settings instance = new Settings();
    UserSettings user;
    String serverAddr;
    String deviceId;
    boolean init;

    public static final int LEVEL_API = 1;
    public static final int PORT = 8081;
    public static final int SERVER_ERROR = -2;
    public static final int CONNECTION_ERROR = -1;
    public static final int LOGIN_CORRECT = 1;
    public static final int JSON_ERROR = -3;

    public static Settings getInstance() {return instance; }

    private Settings() {
        user = new UserSettings();
        String serverAddr = "localhost/TWY";
        deviceId = "1";
        init = false;
    }

    public void setUserName(String name) {
        user.setLogin(name);
    }
    public void setUserToken(String token) {
        user.setToken(token);
    }
    public void setServerAddr(String addr) {
        this.serverAddr = addr;
    }
    public void setUserPass(String pass) {
        user.setPass(pass);
    }
    public void setDevId(String deviceId) {this.deviceId = deviceId; init = true; }

    public String getServerAddr() { return this.serverAddr; }
    public String getUserName() { return this.user.getLogin(); }
    public String getUserToken() { return this.user.getToken(); }
    public String getUserPass() {
        return this.user.getPass();
    }
    public String getDeviceId() { return deviceId; }
    public boolean isInit() { return init; }
}

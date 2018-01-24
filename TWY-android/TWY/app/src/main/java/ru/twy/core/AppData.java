package ru.twy.core;

import android.app.Activity;
import android.content.SharedPreferences;

/**
 * Created by irit on 05.11.16.
 */

public class AppData {

    SharedPreferences sPref;
    SharedPreferences.Editor editor;
    private static AppData ourInstance = new AppData();

    final String SERVER_ADDR = "ServerAddr";

    public static AppData getInstance() {
        return ourInstance;
    }

    private AppData() {
    }


    //Метод для сохранения данных
    public void saveData(SharedPreferences prefs) {
        sPref = prefs;
        editor = sPref.edit();
        saveServerAddress();
        editor.commit();
    }


    //Метод для загрузки данных
    public void loadData(SharedPreferences prefs) {
        sPref = prefs;
        editor = sPref.edit();
        loadServerAddress();
    }

    /*
        =================================================================================
     */

    protected void saveServerAddress() {
        editor.putString(SERVER_ADDR, Settings.getInstance().getServerAddr() );
    }
    protected void loadServerAddress() {
        Settings.getInstance().setServerAddr(sPref.getString(SERVER_ADDR, "localhost/TWY") );
    }
}

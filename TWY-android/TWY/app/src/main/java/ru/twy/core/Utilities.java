package ru.twy.core;

import android.app.Activity;
import android.content.Intent;

import ru.twy.*;

/**
 * Created by irit on 05.11.16.
 */

public class Utilities {


    public static Intent loadLoginPage(Activity source) {
        return new Intent(source, Login.class);
    }
    public static Intent loadMainPage(Activity source) {
        return new Intent(source, Main.class);
    }

    public static Intent loadSettingPage(Activity source) {
        return new Intent(source, SettingsPage.class);
    }
}

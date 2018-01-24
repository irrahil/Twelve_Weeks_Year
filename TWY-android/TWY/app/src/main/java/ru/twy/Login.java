package ru.twy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.*;

import ru.twy.core.*;

public class Login extends AppCompatActivity {

    Settings settings = Settings.getInstance();
    API api = API.getInstance();
    AppData data = AppData.getInstance();
    private static boolean firstStart = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (firstStart) {
            loadData();
            firstStart = false;
        }
        init();
    }


    private void loadData() {
        data.loadData(getPreferences(MODE_PRIVATE) );
        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        settings.setDevId(tm.getDeviceId() );
    }


    private void init() {
        ( (Button) findViewById(R.id.loginButton) ).setOnClickListener(init_loginButton_listener() );
        ( (Button) findViewById(R.id.showSettingsbutton) ).setOnClickListener(init_settingButton_listener() );
    }


    private OnClickListener init_loginButton_listener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setUserName( ( (EditText) findViewById(R.id.loginField) ).getText().toString() );
                settings.setUserPass( ( (EditText) findViewById(R.id.passField) ).getText().toString() );
                int result = API.getInstance().LoginToServer(true);
                if (result == Settings.LOGIN_CORRECT ) {
                    if (settings.getUserToken().length() > 31) {    //Фиксируем что наш полученный токен соответствует размеру
                        startActivity(Utilities.loadMainPage(Login.this) );
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Не удалось получить токен. Неверный логин или пароль.",
                                Toast.LENGTH_SHORT);
                        toast.show();
                    }

                } else if (result == Settings.CONNECTION_ERROR) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не удалось войти в систему. Проверьте настройки соединения.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (result == Settings.SERVER_ERROR) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Не удалось войти в систему. Сервер не отвечает. Повторите позже.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                } else if (result == Settings.JSON_ERROR) {
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Ошибка разбора данных.",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
        };
    }

    private OnClickListener init_settingButton_listener() {
        return new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(Utilities.loadSettingPage(Login.this) );
            }
        };
    }


    public void onBackPressed() {
        openQuitDialog();
    }


    protected void onDestroy() {
        data.saveData(getPreferences(MODE_PRIVATE) );
        super.onDestroy();
    }



    private void openQuitDialog() {
        AlertDialog.Builder quitDialog = new AlertDialog.Builder(
                Login.this);
        quitDialog.setTitle("Выход: Вы уверены?");

        quitDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        quitDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });

        quitDialog.show();
    }
}

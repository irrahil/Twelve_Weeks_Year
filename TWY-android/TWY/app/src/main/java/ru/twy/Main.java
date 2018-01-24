package ru.twy;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.twy.core.*;

public class Main extends AppCompatActivity {


    Settings settings = Settings.getInstance();
    API api = API.getInstance();
    AppData data = AppData.getInstance();


    //===========================================================================================//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);             //Восстанавливаем предыдущее состояние
        setContentView(R.layout.activity_main_page);
        init();
    }



    //Инициализация всех необходимых параметров для работы приложения
    private void init() {

    }



    /*
        Уничтожение объекта приложения
     */
    protected void onDestroy() {
        data.saveData(getPreferences(MODE_PRIVATE) );
        super.onDestroy();
    }


}

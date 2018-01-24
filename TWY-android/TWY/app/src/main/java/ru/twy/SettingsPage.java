package ru.twy;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.*;
import android.widget.Button;
import android.widget.TextView;

import ru.twy.core.*;

public class SettingsPage extends AppCompatActivity {

    Settings settings = Settings.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }



    private void init() {
        ( (Button) findViewById(R.id.setSettingsButton) ).setOnClickListener(init_settingAndReturnButton_listener() );
        ( (TextView) findViewById(R.id.tokenField) ).setText( settings.getUserToken() );
        ( (TextView) findViewById(R.id.serverAddrField) ).setText( settings.getServerAddr() );
    }



    private OnClickListener init_settingAndReturnButton_listener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settings.setServerAddr( ( (TextView) findViewById(R.id.serverAddrField) ).getText().toString() );
                startActivity(Utilities.loadLoginPage(SettingsPage.this) );
            }
        };
    }
}

package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import android.widget.EditText;

public class WifiSetup extends AppCompatActivity {
    private EditText ssid;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setup);
    }
}

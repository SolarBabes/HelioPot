package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;

public class WifiSetup extends AppCompatActivity {
    String IP = "192.168.5.1";
    int port = 8765;
    private EditText ssid;
    private EditText password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setup);

        ssid = (EditText) findViewById(R.id.username);
    }

    public void send(View v) {
        TCPSender tcpSender = new TCPSender();
        tcpSender.execute(ssid.getText().toString());
    }

    public void goToPlantDetail(View view) {
        Intent intent = new Intent(this, PlantDetail.class);
        startActivity(intent);
    }
}

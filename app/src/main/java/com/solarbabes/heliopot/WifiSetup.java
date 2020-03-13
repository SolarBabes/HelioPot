package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class WifiSetup extends AppCompatActivity {
    String IP = "192.168.5.1";
    int port = 8765;
    private EditText ssid;
    private EditText password;
    private UDPUtils udpUtils;
    private Thread thread;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setup);

        Toolbar toolbar = findViewById(R.id.toolbarwifi);
        toolbar.setTitle("Wifi setting");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ssid = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        udpUtils = new UDPUtils("192.168.5.1",8765);
    }


    public void sendWifi(View view) {
        if (ssid.getText().toString()==null || ssid.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Please enter SSID.", Toast.LENGTH_SHORT).show();
            return;
        }else if (password.getText().toString()==null || password.getText().toString().length()==0){
            Toast.makeText(getApplicationContext(), "Please enter password.", Toast.LENGTH_SHORT).show();
            return;
        }
        new Thread(){
            @Override
            public void run(){
                udpUtils.sendControInfo(ssid.getText().toString(),password.getText().toString());
                Toast.makeText(getApplicationContext(), "Wi-fi info received successfully", Toast.LENGTH_SHORT).show();
            }
        }.start();
        thread =  new Thread(udpUtils);
        thread.start();
        Intent intent = new Intent(this, PlantList.class);
        startActivity(intent);
        //TODO plant list page should finsh
        // finish(); ORDER IS WRONG NOW!!!!!!
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

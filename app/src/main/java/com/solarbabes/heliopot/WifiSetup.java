package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private int status=0;// 1:connected


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_setup);

        Toolbar toolbar = findViewById(R.id.toolbarwifi);
        toolbar.setTitle("Wi-Fi Setup");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(WifiSetup.this);
        builder.setCancelable(true);
        builder.setTitle("Please connect to your HelioPot's Wi-Fi");
        builder.setMessage("Open your device's Wi-Fi settings and connect to the HelioPot's Wi-fi." +
                " Once this is done, press next.");

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        });

        builder.setPositiveButton("Next", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();


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
                status =1;
            }
        }.start();
        thread =  new Thread(udpUtils);
        thread.start();

        AlertDialog.Builder builder = new AlertDialog.Builder(WifiSetup.this);

        builder.setCancelable(false);
        builder.setTitle("Success!");
        builder.setMessage("The HelioPot is now connected to your Wi-Fi. Please connect your device" +
                        " back to your home Wi-Fi and press finish.");

        builder.setPositiveButton("Finish", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), PlantList.class);
                startActivity(intent);
                finish();
                //TODO plant list page should finsh
                // finish(); ORDER IS WRONG NOW!!!!!!
            }
        });
        builder.show();
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

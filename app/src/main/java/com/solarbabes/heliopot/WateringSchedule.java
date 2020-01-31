package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class WateringSchedule extends AppCompatActivity {
    public static String interval;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watering_schedule);

        EditText intervalBox = (EditText) findViewById(R.id.interval);
        Button sendButton = (Button) findViewById(R.id.sendButton);

        if (interval != null) {
            intervalBox.setText(interval);
            sendButton.callOnClick();
        }
    }

    public void onStop() {
        super.onStop();

        EditText intervalBox = (EditText) findViewById(R.id.interval);
        interval = intervalBox.getText().toString();
    }

    public void onButtonClick(View v) {
        EditText value = (EditText) findViewById(R.id.interval);
        TextView t1 = (TextView) findViewById(R.id.textView);
        TextView t2 = (TextView) findViewById(R.id.textView2);

        final long ONE_MINUTE_IN_MILLIS = 60000;

        Calendar date = Calendar.getInstance();
        long t = date.getTimeInMillis();

        int valueNo = Integer.parseInt(value.getText().toString());
        Date nextWateringTime = new Date(t + valueNo * ONE_MINUTE_IN_MILLIS);

        t1.setText(nextWateringTime.toString());
        t2.setText("Plant will be watered at:");
    }

//    @Override
//    public void onBackPressed() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
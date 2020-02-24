package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.CompoundButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PlantDetail extends AppCompatActivity {
    private static String plantName;
    public static final String EXTRA_MESSAGE = "com.solarbabes.heliopot.PLANT_NAME";
    public static final String PLANT_ID = "com.solarbabes.heliopot.PLANT_ID";
    // TODO delete static
    private static TextView temperature_view ;
    private static TextView humidity_view ;
    private static TextView moisture_view ;
    private String plantId;
    private Switch switch1 ;
    private EditText Interval;
    private Button send;
    private int interval = 0;
    private TextView FirstWateringTime;
    private TextView SecondWateringTime;
    private TextView ThirdWateringTime;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // The activity is started with just the name given from the clicked item in the list.
        // Using the name, we retrieve all stats.
        Intent intent = getIntent();
//        plantName = intent.getStringExtra(PlantList.PLANT_NAME);
        plantId = intent.getStringExtra(PlantList.PLANT_ID);
        // Gives us access to the stored data children.
        mDatabase = FirebaseDatabase.getInstance().getReference("heliopots/"+plantId+"/data/realtime");
//        mDatabase = FirebaseDatabase.getInstance().getReference("bot/"+plantName+"/realtime");
        mDatabase.addListenerForSingleValueEvent(WateringListener);
        mDatabase.addValueEventListener(Listener);

        // Obtaining references to UI elements.
        temperature_view = findViewById(R.id.textView_temperature);
        humidity_view = findViewById(R.id.textView_humidity);
        moisture_view = findViewById(R.id.textView_moisture);
        switch1 = (Switch) findViewById(R.id.switch1);
        Interval = findViewById(R.id.interval);
        send = findViewById(R.id.sendButton);
        FirstWateringTime = findViewById(R.id.FirstWateringTime);
        SecondWateringTime = findViewById(R.id.SecondWateringTime);
        ThirdWateringTime = findViewById(R.id.ThirdWateringTime);

        // Switching between manual and automatic watering time views.
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("switch",Boolean.toString(b));
                if (b){
                    send.setVisibility(View.VISIBLE);
                    Interval.setVisibility(View.VISIBLE);
                }else{
                    send.setVisibility(View.GONE);
                    Interval.setVisibility(View.GONE);
                    mDatabase.child("interval").setValue(-1);
                }

            }
        });
    }

    // Updates three sample watering times when a new interval is given.
    ValueEventListener WateringListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            interval = Integer.parseInt(dataSnapshot.child("interval").getValue().toString());
            if (interval>=0){
                switch1.setChecked(true);
                send.setVisibility(View.VISIBLE);
                Interval.setVisibility(View.VISIBLE);
            }else{
                switch1.setChecked(false);
                send.setVisibility(View.GONE);
                Interval.setVisibility(View.GONE);
            }
            FirstWateringTime.setText(sdf.format(new Date(Long.parseLong(dataSnapshot.child("water1").getValue().toString())*1000)));
            SecondWateringTime.setText(sdf.format(new Date(Long.parseLong(dataSnapshot.child("water2").getValue().toString())*1000)));
            ThirdWateringTime.setText(sdf.format(new Date(Long.parseLong(dataSnapshot.child("water3").getValue().toString())*1000)));
            Interval.setText(Integer.toString(interval/60));
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Map<String, Long> map = (Map<String, Long>) dataSnapshot.getValue();
            temperature_view.setText(map.get("temperature").toString()+"°C");
            humidity_view.setText(map.get("humidity").toString()+"%");
            moisture_view.setText(map.get("moisture").toString()+"%");
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };

    private DatabaseReference mDatabase;

    public void sendWateringTime(View view){
        interval = Integer.parseInt(Interval.getText().toString())*60;
        mDatabase.child("interval").setValue(Math.round(Float.parseFloat(Interval.getText().toString())*60));
        mDatabase.child("settingtime").setValue(System.currentTimeMillis()/1000);
        FirstWateringTime.setText(sdf.format(new Date(System.currentTimeMillis()+interval*1000)));
        SecondWateringTime.setText(sdf.format(new Date(System.currentTimeMillis()+interval*2000)));
        ThirdWateringTime.setText(sdf.format(new Date(System.currentTimeMillis()+interval*3000)));
    }

    /** Called when the user taps the Send button */
    public void goToMetric(View view) {
        Intent intent = new Intent(this, MetricData.class);
        String message = plantId;
        intent.putExtra(PLANT_ID, message);
        Log.d("detail go to metric",message);
        startActivity(intent);
    }

    public void goToExtraInfo(View view) {
        Intent intent = new Intent(this, PlantExtraInfo.class);
        String message = plantName;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToGlallery(View view) {
        Intent intent = new Intent(this, PlantGallery.class);
        startActivity(intent);
    }
    public void goToWateringSchedule(View view) {
        Intent intent = new Intent(this, WateringSchedule.class);
        startActivity(intent);
    }

//    @Override
//    public void finish() {
//        super.finish();
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDatabase.removeEventListener(Listener);
        mDatabase.removeEventListener(WateringListener);
    }


}

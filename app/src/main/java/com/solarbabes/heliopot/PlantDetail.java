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

import java.util.Map;

public class PlantDetail extends AppCompatActivity {
    private static String plantName;
    public static final String EXTRA_MESSAGE = "com.solarbabes.heliopot.MESSAGE";
    // TODO delete static
    private static TextView temperature_view ;
    private static TextView humidity_view ;
    private static TextView moisture_view ;
    private static TextView light_view ;
    private Switch switch1 ;
    private EditText Interval;
    private Button send;
    private int interval = 0;

    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
//            Log.d("111",dataSnapshot.child("interval").toString());
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
            Interval.setText(Integer.toString(interval/60));
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };
    ValueEventListener WateringListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            Map<String, Long> map = (Map<String, Long>) dataSnapshot.getValue();
//                Log.d("map",map.toString());
            temperature_view.setText(map.get("temperature").toString()+"°C");
            humidity_view.setText(map.get("humidity").toString()+"%");
            moisture_view.setText(map.get("moisture").toString()+"%");
            light_view.setText(map.get("light").toString()+"lux");

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        plantName = intent.getStringExtra(PlantList.PLANT_NAME);
        mDatabase = FirebaseDatabase.getInstance().getReference("bot/"+plantName+"/realtime");
        mDatabase.addListenerForSingleValueEvent(WateringListener);
        mDatabase.addValueEventListener(Listener);


        temperature_view = findViewById(R.id.textView_temperature);
        humidity_view = findViewById(R.id.textView_humidity);
        moisture_view = findViewById(R.id.textView_moisture);
        light_view = findViewById(R.id.textView_light);
        switch1 = (Switch) findViewById(R.id.switch1);
        Interval = findViewById(R.id.interval);
        send = findViewById(R.id.sendButton);

        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.d("switch",Boolean.toString(b));
//                send.setEnabled(b);
//                Interval.setEnabled(b);
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





    public void sendWateringTime(View view){
        mDatabase.child("interval").setValue(Integer.parseInt(Interval.getText().toString())*60);
    }

    /** Called when the user taps the Send button */
    public void goToMetric(View view) {
        Intent intent = new Intent(this, MetricData.class);
        String message = plantName;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void goToExtraInfo(View view) {
        Intent intent = new Intent(this, PlantExtraInfo.class);
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

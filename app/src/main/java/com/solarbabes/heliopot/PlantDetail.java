package com.solarbabes.heliopot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class PlantDetail extends AppCompatActivity {
    private static String plantName;
    public static final String EXTRA_MESSAGE = "com.solarbabes.heliopot.MESSAGE";
    private static TextView temperature_view ;
    private static TextView humidity_view ;
    private static TextView moisture_view ;
    private static TextView light_view ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        plantName = intent.getStringExtra(PlantList.EXTRA_MESSAGE);


        temperature_view = findViewById(R.id.textView_temperature);
        humidity_view = findViewById(R.id.textView_humidity);
        moisture_view = findViewById(R.id.textView_moisture);
        light_view = findViewById(R.id.textView_light);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("bot/"+plantName+"/realtime");
        ValueEventListener Listener = new ValueEventListener() {
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
        mDatabase.addValueEventListener(Listener);


    }



    /** Called when the user taps the Send button */
    public void goToMetric(View view) {
        Intent intent = new Intent(this, MetricData.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
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

}

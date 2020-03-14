package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
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
    private static String username;
    public static final String EXTRA_MESSAGE = "com.solarbabes.heliopot.PLANT_NAME";
    public static final String PLANT_NAME = "com.solarbabes.heliopot.PLANT_NAME";
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // The activity is started with just the name given from the clicked item in the list.
        // Using the name, we retrieve all stats.
        Intent intent = getIntent();
        plantName = intent.getStringExtra("PLANT_NAME");
        plantId = intent.getStringExtra(PlantList.PLANT_ID);
        username = intent.getStringExtra("USERNAME");
        plantName = intent.getStringExtra(PLANT_NAME);

        Toolbar toolbar = findViewById(R.id.toolbardetail);
        toolbar.setTitle(plantName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        // Gives us access to the stored data children.
        mDatabase = FirebaseDatabase.getInstance().getReference("heliopots/"+plantId+"/data/realtime");
//        mDatabase = FirebaseDatabase.getInstance().getReference("bot/"+plantName+"/realtime");
        mDatabase.addValueEventListener(Listener);

        // Obtaining references to UI elements.
        temperature_view = findViewById(R.id.textView_temperature);
        humidity_view = findViewById(R.id.textView_humidity);
        moisture_view = findViewById(R.id.textView_moisture);
        send = findViewById(R.id.sendButton);
    }

    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            Map<String, Long> map = (Map<String, Long>) dataSnapshot.getValue();
            temperature_view.setText(map.get("temperature").toString()+"°C");
            humidity_view.setText(map.get("humidity").toString()+"%");
            moisture_view.setText(map.get("moisture").toString());
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            // Getting Post failed, log a message
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };

    private DatabaseReference mDatabase;

    /** Called when the user taps the Send button */
    public void goToMetric(View view) {
        Intent intent = new Intent(this, MetricData.class);
        String message = plantId;
        intent.putExtra(PLANT_ID, message);
        intent.putExtra(PLANT_NAME, plantName);
        startActivity(intent);
    }

    public void goToExtraInfo(View view) {
        Intent intent = new Intent(this, PlantExtraInfo.class);
        intent.putExtra(PLANT_NAME, plantName);
        startActivity(intent);
    }

    public void goToGallery(View view) {
        Intent intent = new Intent(this, PlantGallery.class);
        intent.putExtra(PLANT_NAME, plantName);
        startActivity(intent);
    }

    public void goToMovement(View view) {
        Intent intent = new Intent(this, ManualControl.class);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.normal_mune, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mDatabase.removeEventListener(Listener);
    }

}

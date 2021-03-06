package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class PlantExtraInfo extends AppCompatActivity {
    public static String plantName;
    public static String plantNotes;
    public static final String PLANT_NAME = "com.solarbabes.heliopot.PLANT_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_extra_info);
        Intent intent = getIntent();
        plantName = intent.getStringExtra(PLANT_NAME);

        Toolbar toolbar = findViewById(R.id.toolbarinfo);
        toolbar.setTitle(plantName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (plantName != null) {
            EditText plantNameBox = (EditText) findViewById(R.id.plantName);
            EditText plantNotesBox = (EditText) findViewById(R.id.plantNotes);

            plantNameBox.setText(plantName);
            plantNotesBox.setText(plantNotes);
        }


    }

    @Override
    protected void onStop() {
        super.onStop();

        Log.d("myTag", "stopped");

        // Saving the entered information so it persists across activity instances.
        EditText plantNameBox = (EditText) findViewById(R.id.plantName);
        EditText plantNotesBox = (EditText) findViewById(R.id.plantNotes);

        plantName = plantNameBox.getText().toString();
        plantNotes = plantNotesBox.getText().toString();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
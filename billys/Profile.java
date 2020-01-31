package com.example.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

public class Profile extends AppCompatActivity {
    public static String plantName;
    public static String plantNotes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

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
}

package com.example.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /** Called when the user taps the watering schedule button */
    public void wateringMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, WateringSchedule.class);
        startActivity(intent);
    }

    /** Called when the user taps the gallery button */
    public void galleryMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Gallery.class);
        startActivity(intent);
    }

    /** Called when the user taps the profile button */
    public void profileMessage(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, Profile.class);
        startActivity(intent);
    }
}

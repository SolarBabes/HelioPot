package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class PlantDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);
    }

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    /** Called when the user taps the Send button */
    public void goToMetric(View view) {
        Intent intent = new Intent(this, MetricData.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}

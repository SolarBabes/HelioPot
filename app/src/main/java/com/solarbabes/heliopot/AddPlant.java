package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddPlant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
    }

    public void onSubmitButtonPress(View view) {
        Intent intent = new Intent(this, PlantList.class);

        // Passing the plant's name back to the plant list activity so the new
        // plant can be added.
        EditText plantName = (EditText) findViewById(R.id.editText_plantName);
        intent.putExtra("PLANT_NAME", plantName.getText().toString());

        // This page is opened with the
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}

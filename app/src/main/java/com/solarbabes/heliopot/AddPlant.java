package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AddPlant extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);
    }

    public void onSubmitButtonPress(View view) {
//        Intent intent = new Intent(this, AddPlant.class);
//        startActivity(intent);
        onBackPressed();
    }

}

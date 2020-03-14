package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class PlantGallery extends AppCompatActivity {
    public static final String PLANT_NAME = "com.solarbabes.heliopot.PLANT_NAME";
    private String plantName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_gallery);
        Intent intent = getIntent();
        plantName = intent.getStringExtra(PLANT_NAME);

        Toolbar toolbar = findViewById(R.id.toolbargallery);
        toolbar.setTitle(plantName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        final GridView gridview = (GridView)findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               int imageID = ((ImageAdapter) gridview.getAdapter()).mThumbIds[position];

               AlertDialog.Builder imageDialog = new AlertDialog.Builder(PlantGallery.this);
               imageDialog.setTitle("Image " + (position + 1));
               ImageView showImage = new ImageView(PlantGallery.this);
               showImage.setImageResource(imageID);
               imageDialog.setView(showImage);

               imageDialog.setNegativeButton("Back", new DialogInterface.OnClickListener()
               {
                   public void onClick(DialogInterface arg0, int arg1)
                   {
                   }
               });
               imageDialog.show();
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
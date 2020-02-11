package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class PlantList extends AppCompatActivity {

    ListView plantList;
    ArrayList<PlantListItem> plantItems;
    PlantListAdapter plantListAdapter;

    private static int backtime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backtime = 0;
        setContentView(R.layout.activity_plant_list);

        plantItems = new ArrayList<PlantListItem>();
        plantList = (ListView) findViewById(R.id.listView_Plants);

        fillArrayList();

        plantListAdapter = new PlantListAdapter(getApplicationContext(), plantItems);

        plantList.setAdapter(plantListAdapter);

        // A click listener for the listView.
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sendMessage(view);
            }
        });
    }

    private void fillArrayList() {
        // Manually add a plant to the list of plants here. It will be added to the listVIew.
        PlantListItem plant_one = new PlantListItem(R.drawable.plant1,
                "Plant 1", "Watering Time: 20:22");
        plantItems.add(plant_one);

        PlantListItem plant_two = new PlantListItem(R.drawable.plant1,
                "Plant 2", "Watering Time: 20:15");
        plantItems.add(plant_two);

        PlantListItem plant_three = new PlantListItem(R.drawable.plant1,
                "Plant 3", "Watering Time: 20:22");
        plantItems.add(plant_three);

        PlantListItem plant_four = new PlantListItem(R.drawable.plant1,
                "Plant 4", "Watering Time: 20:15");
        plantItems.add(plant_four);

        PlantListItem plant_five = new PlantListItem(R.drawable.plant1,
                "Plant 5", "Watering Time: 20:22");
        plantItems.add(plant_five);

        PlantListItem plant_six = new PlantListItem(R.drawable.plant1,
                "Plant 6", "Watering Time: 20:15");
        plantItems.add(plant_six);

        PlantListItem plant_seven = new PlantListItem(R.drawable.plant1,
                "Plant 7", "Watering Time: 20:22");
        plantItems.add(plant_seven);

        PlantListItem plant_eight = new PlantListItem(R.drawable.plant1,
                "Plant 8", "Watering Time: 20:15");
        plantItems.add(plant_eight);

        PlantListItem plant_nine = new PlantListItem(R.drawable.plant1,
                "Plant 9", "Watering Time: 20:15");
        plantItems.add(plant_nine);
    }


    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    /** Called when the user taps the Send button */
    // CURRENTLY: If ANY plant is clicked - take to the SAME PlantDetail activity.
    // Later, each plant should have a UNIQUE PAGE.
    public void sendMessage(View view) {
        backtime = 0;
        Intent intent = new Intent(getApplicationContext(), PlantDetail.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    // To be called when the + button is clicked.
    public void addPlant(View view) {
        backtime = 0;
        Intent intent = new Intent(this, AddPlant.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        if (backtime==0){
            Toast.makeText(getApplicationContext(), "Press again to exit the APP", Toast.LENGTH_LONG).show();
            backtime ++;
        }else{
            super.onBackPressed();
        }

    }
}

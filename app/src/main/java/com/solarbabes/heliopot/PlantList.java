package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class PlantList extends AppCompatActivity {

    ListView plantList;
    ArrayList<PlantListItem> plantItems;
    PlantListAdapter plantListAdapter;
    ArrayList<String> plantName = new ArrayList<String>();

    private static int backtime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backtime = 0;
        setContentView(R.layout.activity_plant_list);

        plantName.add("plant1");
        plantName.add("plant2");
        plantName.add("plant3");
        plantName.add("plant4");
        plantName.add("plant5");
        plantName.add("plant6");
        plantName.add("plant7");
        plantName.add("plant8");
        plantName.add("plant9");
        plantName.add("plant10");
        plantName.add("plant11");

        plantItems = new ArrayList<PlantListItem>();
        plantList = (ListView) findViewById(R.id.listView_Plants);

        fillArrayList();

        plantListAdapter = new PlantListAdapter(getApplicationContext(), plantItems);

        plantList.setAdapter(plantListAdapter);

        // A click listener for the listView.
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToPlantDetail(position);
            }
        });
    }

    private void fillArrayList() {
        // Manually add a plant to the list of plants here. It will be added to the listVIew.

        for (String n:plantName){
            PlantListItem plant_one = new PlantListItem(R.drawable.plant1,
                    n, "Watering Time: 20:22");
            plantItems.add(plant_one);
        }

    }


    public static final String PLANT_NAME = "com.solarbabes.heliopot.MESSAGE";

    /** Called when the user taps the Send button */
    // CURRENTLY: If ANY plant is clicked - take to the SAME PlantDetail activity.
    // Later, each plant should have a UNIQUE PAGE.
    public void goToPlantDetail(int position) {
        backtime = 0;
        Intent intent = new Intent(getApplicationContext(), PlantDetail.class);
        intent.putExtra(PLANT_NAME, plantName.get(position));
        startActivity(intent);
    }

    // To be called when the + button is clicked.
    public void addPlant(View view) {
        backtime = 0;
        Intent intent = new Intent(this, Main2Activity.class);
//        EditText editText = (EditText) findViewById(R.id.editText);
//        String message = editText.getText().toString();
//        intent.putExtra(PLANT_NAME, message);
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

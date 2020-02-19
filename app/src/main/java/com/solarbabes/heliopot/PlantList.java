package com.solarbabes.heliopot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;


public class PlantList extends AppCompatActivity {

    ListView plantList;
    ArrayList<PlantListItem> plantItems = new ArrayList<>();
    PlantListAdapter plantListAdapter;
    ArrayList<String> plantNames = new ArrayList<String>();
    private DatabaseReference mDatabase;

    private static int backtime = 0;
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initial setup of layout.
        super.onCreate(savedInstanceState);
        backtime = 0;
        setContentView(R.layout.activity_plant_list);

        // Username is passed in from Login Activity.
        // Retrieving plants stored for this username.
        Intent intent = getIntent();
        username = intent.getStringExtra(Login.USERNAME);
        username = username.replaceAll("[^a-zA-Z0-9]","");
        mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);

        // A listener for database values updated.
        // Events dealt with by the overridden Listener below.
        mDatabase.addValueEventListener(Listener);

        // Populating list with retrieved plants (via adapter).
        plantList = (ListView) findViewById(R.id.listView_Plants);
//        updatePlants();

        // A listener for clicking items in the list.
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToPlantDetail(position);
            }
        });
    }

    // Listener for database updates.
    ValueEventListener Listener = new ValueEventListener() {
        @Override
        // If it is detected that data in the database has changed, update the plant list.
        public void onDataChange(DataSnapshot dataSnapshot) {

            updatePlants(dataSnapshot);

//            for (DataSnapshot plant : dataSnapshot.child("plants").getChildren()) {
//                String name = plant.child("name").getValue().toString();
//                if (!plantNames.contains(name)){
//                    plantNames.add(name);
//                }
//                updatePlants();
//            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };


    private void updatePlants(DataSnapshot update){
        //TODO here, if one plant is added, it invokes setAdapter to refresh it, but there is something wrong
        // so when adding new plant, only action is change the online database

        // Filling the list with new plants retrieved from the database.
        // Each time a change is detected, the list is completely remade.

        plantItems = new ArrayList<>();

        for (DataSnapshot plant : update.child("plants").getChildren()) {
            //TODO Add real values to retrieve from server for picID & watering time.
            int picID = R.drawable.plant1;
            String wateringTime = "Watering Time: 20:22";
            String name = plant.child("name").getValue().toString();
            plantItems.add(new PlantListItem(picID, name, wateringTime));
        }

        plantList.setAdapter(new PlantListAdapter(this, plantItems));
    };

    // A listener for new plants being passed back from the 'AddPlant' activity.
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String p = data.getStringExtra("PLANT_NAME");
                // Note most of the added plant is hardcoded for now.
                PlantListItem newPlant = new PlantListItem(R.drawable.plant2, p,
                        "Watering Time: 20:00");

                plantItems.add(newPlant);
                plantNames.add(p);
                plantListAdapter.notifyDataSetChanged();
            }
        }
    }




    public static final String PLANT_NAME = "com.solarbabes.heliopot.MESSAGE";

    /** Called when the user taps the Send button */
    // CURRENTLY: If ANY plant is clicked - take to the SAME PlantDetail activity.
    // Later, each plant should have a UNIQUE PAGE.
    public void goToPlantDetail(int position) {
        backtime = 0;
        Intent intent = new Intent(getApplicationContext(), PlantDetail.class);
        intent.putExtra(PLANT_NAME, plantNames.get(position));
        startActivity(intent);
    }

    // To be called when the + button is clicked.
    public void addPlant(View view) {
        backtime = 0;
        Intent intent = new Intent(this, AddPlant.class);
        // RequestCode currently hardcoded. CHANGE LATER.
        startActivityForResult(intent, 1);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        mDatabase.removeEventListener(Listener);
        if (backtime==0){
            Toast.makeText(getApplicationContext(), "Press again to exit the APP", Toast.LENGTH_LONG).show();
            backtime ++;
        }else{
            super.onBackPressed();
        }

    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case R.id.action_logout:
//                // User chose the "Favorite" action, mark the current item
//                // as a favorite...
//                Toast.makeText(getApplicationContext(),"hhhhhhhh", Toast.LENGTH_LONG).show();
//                return true;
//
//            default:
//                // If we got here, the user's action was not recognized.
//                // Invoke the superclass to handle it.
//                return super.onOptionsItemSelected(item);
//
//        }
//    }

}

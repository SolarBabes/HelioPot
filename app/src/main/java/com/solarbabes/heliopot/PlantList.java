package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class PlantList extends AppCompatActivity {

    ListView plantList;
    ArrayList<PlantListItem> plantItems = new ArrayList<PlantListItem>();
    PlantListAdapter plantListAdapter;
    ArrayList<String> plantName = new ArrayList<String>();
    private DatabaseReference mDatabase;
    ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {

            for (DataSnapshot postSnapshot: dataSnapshot.child("plant").getChildren()) {
                Log.e("Get Data", postSnapshot.child("name").getValue().toString());
                String name = postSnapshot.child("name").getValue().toString();
                if (!plantName.contains(name)){
                    plantName.add(name);
                }
                setListAdapter();
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };

    private static int backtime = 0;
    public static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        backtime = 0;
        setContentView(R.layout.activity_plant_list);
        Intent intent = getIntent();
        username = intent.getStringExtra(Login.EXTRA_MESSAGE);
        Log.d("username",username);
        Log.d("username",Integer.toString(username.length()));
        mDatabase = FirebaseDatabase.getInstance().getReference("user/"+username);
        mDatabase.addValueEventListener(Listener);

//        plantName.add("plant1");

        plantList = (ListView) findViewById(R.id.listView_Plants);

        setListAdapter();

        // A click listener for the listView.
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToPlantDetail(position);
            }
        });
    }
    private void setListAdapter(){
        //TODO here, if one plant is added, it invokes setAdapter to refresh it, but there is something wrong
        // so when adding new plant, only action is change the online database
        fillArrayList();
        plantList.setAdapter(new PlantListAdapter(this, plantItems));
    };

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
        Intent intent = new Intent(this, AddPlant.class);
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
        mDatabase.removeEventListener(Listener);
        if (backtime==0){
            Toast.makeText(getApplicationContext(), "Press again to exit the APP", Toast.LENGTH_LONG).show();
            backtime ++;
        }else{
            super.onBackPressed();
        }

    }

}

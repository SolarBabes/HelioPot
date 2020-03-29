package com.solarbabes.heliopot;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;


public class PlantList extends AppCompatActivity {

    ListView plantList;
    ArrayList<PlantListItem> plantItems = new ArrayList<>();
    HashMap<String, String> idKey = new HashMap<>();
    ArrayList<String> ownedPlants = new ArrayList<>();
    private DatabaseReference mDatabase;
    private DatabaseReference userRef; // USED FOR GETTING OWNED IDs.
    private String removeID ;

    private static int backtime = 0;
    public static String username;
    private static AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initial setup of layout.
        super.onCreate(savedInstanceState);
        backtime = 0;
        setContentView(R.layout.activity_plant_list);
        Toolbar toolbar = findViewById(R.id.toolbarlist);
//        toolbar.setTitle("title");
        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.logo);

        // Username is passed in from Login Activity.
        // Retrieving plants stored for this username.
        // Check for null necessary because this activity can also be started from activities
        // other than login. In that case - they don't have a username extra.
        Intent intent = getIntent();
        if (intent.getStringExtra(Login.USERNAME) != null) {
            Log.d("NULL_INTENT", "Intent was null");
            username = intent.getStringExtra(Login.USERNAME);
            username = username.replaceAll("[^a-zA-Z0-9]","");
        }

        // A listener for new heliopot IDs added to the user account.
        userRef = FirebaseDatabase.getInstance().getReference("user/" + username);
        userRef.addValueEventListener(IDListener);

        // A listener for database values updated.
        // Events dealt with by the overridden Listener below.
        mDatabase = FirebaseDatabase.getInstance().getReference("heliopots");
        mDatabase.addValueEventListener(Listener);

        // Populating list with retrieved plants (via adapter).
        plantList = (ListView) findViewById(R.id.listView_Plants);

        // A listener for clicking items in the list.
        plantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                goToPlantDetail(position);
            }
        });

        plantList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long l) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PlantList.this);
                removeID=ownedPlants.get(position);
                builder.setCancelable(true);
                builder.setTitle("Remove plant");
                builder.setMessage("Do you want to remove "+getPlantName(ownedPlants.get(position))+"?");

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                builder.setPositiveButton("REMOVE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removePlant(removeID);
                    }
                });
                builder.show();
                return true;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dialog != null) {
            dialog.cancel();
        }
    }

    private void removePlant(String id){
        Log.d("!23",idKey.get(id));
        userRef.child("ownedPots").child(idKey.get(id)).removeValue();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }


    // Listener for database updates.
    ValueEventListener Listener = new ValueEventListener() {
        @Override
        // If it is detected that data in the database has changed, update the plant list.
        public void onDataChange(DataSnapshot dataSnapshot) {

            updatePlants(dataSnapshot);
            updateList();

        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };

    ValueEventListener IDListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            ArrayList<String> IDs = new ArrayList<>();
            DataSnapshot IDRoot = dataSnapshot.child("ownedPots");

            for (DataSnapshot ID : IDRoot.getChildren()) {
                IDs.add(ID.getValue().toString());
                idKey.put(ID.getValue().toString(), ID.getKey().toString());
            }
            ownedPlants = IDs;

            updateList();
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {}
    };

    private void updateList() {
        ArrayList<PlantListItem> plantsToShow = new ArrayList<>();

        for (String ID : ownedPlants) {
            for (PlantListItem plant : plantItems) {
                if (plant.getID().equals(ID)) {
                    plantsToShow.add(plant);
                }
            }
        }

        TextView emptyList = (TextView) findViewById(R.id.textView_Empty_PlantList);
        if (plantsToShow.size() == 0) {
            emptyList.setVisibility(View.VISIBLE);
        }
        else {
            emptyList.setVisibility(View.GONE);
        }

        plantList.setAdapter(new PlantListAdapter(this, plantsToShow));
    }

    private void updatePlants(DataSnapshot update){
        //TODO here, if one plant is added, it invokes setAdapter to refresh it, but there is something wrong
        // so when adding new plant, only action is change the online database

        // Filling the list with new plants retrieved from the database.
        // Each time a change is detected, the list is completely remade.

        plantItems = new ArrayList<>();

        for (DataSnapshot heliopot : update.getChildren()) {
            //TODO Add real values to retrieve from server for picID & watering time.
            int picID = R.drawable.plant1;
            String wateringTime = heliopot.child("message").getValue().toString();
            String name = heliopot.child("name").getValue().toString();
            String ID = heliopot.child("id").getValue().toString();
            plantItems.add(new PlantListItem(ID, picID, name, wateringTime));
        }
    };

    public static final String PLANT_NAME = "com.solarbabes.heliopot.PLANT_NAME";
    public static final String PLANT_ID = "com.solarbabes.heliopot.PLANT_ID";

    /** Called when the user taps the Send button */
    // CURRENTLY: If ANY plant is clicked - take to the SAME PlantDetail activity.
    // Later, each plant should have a UNIQUE PAGE.
    public void goToPlantDetail(int position) {
        backtime = 0;
        Intent intent = new Intent(getApplicationContext(), PlantDetail.class);
        //intent.putExtra(PLANT_NAME, "TEST");
//        intent.putExtra(PLANT_NAME, plantNames.get(position));
        intent.putExtra(PLANT_NAME, plantItems.get(position).getName());//this works
        intent.putExtra(PLANT_ID, ownedPlants.get(position));//this works
        intent.putExtra(PLANT_NAME, getPlantName(ownedPlants.get(position)));
        intent.putExtra("USERNAME", username);
        startActivity(intent);
    }

    private String getPlantName(String id){
        for (PlantListItem plantItem : plantItems) {
            if (id.equals(plantItem.getID())){
                return plantItem.getName();
            }
        }
        return "";
    }

    // To be called when the + button is clicked.
    public void addPlant(View view) {

        ///// Integrating movement setup on creation. ////////
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(PlantList.this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_new_plant, null);

        dialogBuilder.setView(dialogView);
        dialog = dialogBuilder.create();
        dialog.show();

        // Alternative way of exiting the dialog. Can also just click outside of the dialog.
        // This checkbox event has to be handled here as it needs access to the dialog object.
        Button cancelButton = dialogView.findViewById(R.id.button_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }

    public void startFullSetup(View view) {
        backtime = 0;
        AddPlant.mapCreated = false;
        Intent intent = new Intent(this, AddPlant.class);
        intent.putExtra("SETUP_TYPE", "FULL");
        intent.putExtra("USERNAME", username);
        intent.putStringArrayListExtra("IDs", ownedPlants);
        startActivity(intent);
    }

    public void startMinorSetup(View view) {
        backtime = 0;
        Intent intent = new Intent(this, AddPlant.class);
        intent.putExtra("SETUP_TYPE", "MINOR");
        intent.putExtra("USERNAME", username);
        intent.putStringArrayListExtra("IDs", ownedPlants);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings){
            File dir = getFilesDir();
            File file = new File(dir,"username.txt");
            if (file.delete()){
                finish();
                Toast.makeText(getApplicationContext(), "Logout succeed!", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, Login.class);
                startActivity(intent);

            }else{
                Toast.makeText(getApplicationContext(), "Logout failed!", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

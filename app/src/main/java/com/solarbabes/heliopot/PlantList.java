package com.solarbabes.heliopot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
//import androidx.navigation.NavController;
//import androidx.navigation.Navigation;


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
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if ("logout".equals(item.getTitle())){
                    Log.d("2323","5235");
                }
                return false;
            }
        });
        setSupportActionBar(toolbar);

//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        actionBar.setHomeAsUpIndicator(R.drawable.logo);
        Intent intent = getIntent();
        username = intent.getStringExtra(Login.EXTRA_MESSAGE);
        username=username.replaceAll("[^a-zA-Z0-9]","");
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
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

    public void save(String name, String text) {
        //        String text = mEditText.getText().toString();

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(name, MODE_PRIVATE);
            fos.write(text.getBytes());

//            mEditText.getText().clear();
//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + name + "---" + text,
//                    Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

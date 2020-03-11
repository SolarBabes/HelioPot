package com.solarbabes.heliopot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AddPlant extends AppCompatActivity {

    private String username;

    // Path for available heliopots.
    private final String HELIOPOT_LOCATION = "heliopots";
    private ArrayList<String> IDs = new ArrayList<>();

    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference(HELIOPOT_LOCATION);
    // Storing each plant as a DataSnaphot.
    private ArrayList<DataSnapshot> heliopots = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        IDs = intent.getExtras().getStringArrayList("IDs");
        Toolbar toolbar = findViewById(R.id.toolbaradd);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Setting a listener for new heliopots.
        // Required so we can check ID and passwords.
        // Note this listener is triggered when initiated also.
        mDatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                heliopots.add(dataSnapshot);
            }

            // Currently unused but have to be overridden.
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    public void onSubmitButtonPress(View view) {
        EditText helioIDView = (EditText) findViewById(R.id.editText_helioPotID);
        EditText passwordView = (EditText) findViewById(R.id.editText_password);
        EditText newPlantName = (EditText) findViewById(R.id.editText_plantName);

        String enteredHelioID = helioIDView.getText().toString();
        String enteredPassword = passwordView.getText().toString();

        // Check ID & PW are legal.
        //TODO more checking.
        if (enteredHelioID.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Fill user & pw", Toast.LENGTH_LONG).show();
        }else if(IDs.contains(enteredHelioID)){
            Toast.makeText(this, enteredHelioID+" is added.", Toast.LENGTH_LONG).show();
        }
        else {
            //TODO Probably do this in a more secure way...
            // Currently all IDs & passwords are downloaded & stored locally, unencrypted.

            boolean pairFound = false;

            // Check ID & PW are correct.
            for (DataSnapshot plant : heliopots) {
                String helioID = plant.child("id").getValue().toString();
                String password = plant.child("password").getValue().toString();

                if (enteredHelioID.equals(helioID) && enteredPassword.equals(password)) {
                    Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();

                    // Add heliopot ID to this user.
                    //TODO only add the ID if it isn't already added.
                    FirebaseDatabase.getInstance().getReference("user/" + username).child("ownedPots").push().setValue(helioID);
                    // Creates child if it doesn't exist.
                    // Push creates a new child with a random key.
//                    user.child("ownedPots").push().setValue(helioID);
                    FirebaseDatabase.getInstance().getReference("heliopots/" + helioID).child("name").setValue(newPlantName.getText().toString());

                    Intent intent = new Intent(this, WifiSetup.class);

                    startActivity(intent);

                    pairFound = true;
                    break;
                }
            }
            if (pairFound == false) {
                // No correct ID & PW match found.
                Toast.makeText(this, "Incorrect ID & PW Pair", Toast.LENGTH_LONG).show();
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
        super.onBackPressed();
    }
}

package com.solarbabes.heliopot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

    public static boolean mapCreated = false;
    private static String setupType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_plant);

        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        IDs = intent.getExtras().getStringArrayList("IDs");
        Toolbar toolbar = findViewById(R.id.toolbaradd);
        toolbar.setTitle("Add HelioPot");
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

        setupType = intent.getStringExtra("SETUP_TYPE");
        // Showing the correct views for the chosen setup type.
        if (setupType.equals("MINOR")) {
            EditText plantNameEditText = findViewById(R.id.editText_plantName);
            Button createSetupButton = findViewById(R.id.button_map_setup);
            Spinner plantTypeSpinner = findViewById(R.id.spinner_plant_type);
            Spinner pictureTimesSpinner = findViewById(R.id.spinner_picture_times);
            TextView setupInstructions = findViewById(R.id.textView_add_plant_instructions);

            plantNameEditText.setVisibility(View.GONE);
            plantTypeSpinner.setVisibility(View.GONE);
            pictureTimesSpinner.setVisibility(View.GONE);
            createSetupButton.setVisibility(View.GONE);

            setupInstructions.setText("You can find your HelioPot ID and password on the base of the robot.");
        } // else don't hide anything.
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (mapCreated == true) {
            Button mapSetupButton = (Button) findViewById(R.id.button_map_setup);
            mapSetupButton.setText("Map Created");
            mapSetupButton.setTextColor(getResources().getColor(R.color.darkgreen));
            mapSetupButton.setClickable(false);
        }
    }

    public void onSubmitButtonPress(View view) {
        EditText helioIDView = (EditText) findViewById(R.id.editText_helioPotID);
        EditText passwordView = (EditText) findViewById(R.id.editText_password);
        EditText newPlantName = (EditText) findViewById(R.id.editText_plantName);
        Spinner plantType = (Spinner) findViewById(R.id.spinner_picture_times);
        Spinner pictureTimes = (Spinner) findViewById(R.id.spinner_picture_times);

        String enteredHelioID = helioIDView.getText().toString();
        String enteredPassword = passwordView.getText().toString();

        boolean fieldsCorrect = false;

        // Checking required fields are filled.
        if (enteredHelioID.isEmpty() || enteredPassword.isEmpty()) {
            Toast.makeText(this, "Fill user & pw", Toast.LENGTH_LONG).show();
        }else if(IDs.contains(enteredHelioID)){
            Toast.makeText(this, enteredHelioID+" is added.", Toast.LENGTH_LONG).show();
        }else if((setupType.equals("FULL")) && (plantType.getSelectedItem().toString().equals("Plant Type"))) {
            Toast.makeText(this, "Please select plant type.", Toast.LENGTH_LONG).show();
        }else if((setupType.equals("FULL")) && (newPlantName.getText().toString().isEmpty())){
            Toast.makeText(this, "Please enter plant name.", Toast.LENGTH_LONG).show();
        } else if((setupType.equals("FULL")) && (mapCreated == false)) {
            Toast.makeText(this, "Please finish map setup.", Toast.LENGTH_LONG).show();
        } else if ((setupType.equals("FULL")) && (pictureTimes.getSelectedItem().toString().equals("Picture Times"))) {
            Toast.makeText(this, "Please select a picture time.", Toast.LENGTH_LONG).show();
        } else {
            fieldsCorrect = true;
        }

        if ((fieldsCorrect) && (setupType.equals("FULL"))) {
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
                    FirebaseDatabase.getInstance().getReference("heliopots/" + helioID).child("type").setValue(plantType.getSelectedItem().toString());

                    next();

                    pairFound = true;
                    break;
                }
            }
            if (pairFound == false) {
                // No correct ID & PW match found.
                Toast.makeText(this, "Incorrect ID & PW Pair", Toast.LENGTH_LONG).show();
            }
        } else if (fieldsCorrect && (setupType.equals("MINOR"))) {
            // In this case we don't have a name etc to update. Just pull existing info.
            // for the given HelioPot and add it to the plant list.
            boolean pairFound = false;

            //TODO Remove duplicate code in this block.
            for (DataSnapshot plant : heliopots) {
                String helioID = plant.child("id").getValue().toString();
                String password = plant.child("password").getValue().toString();

                if (enteredHelioID.equals(helioID) && enteredPassword.equals(password)) {
                    Toast.makeText(this, "Correct!", Toast.LENGTH_LONG).show();

                    // Add heliopot ID to this user.
                    //TODO only add the ID if it isn't already added.
                    FirebaseDatabase.getInstance().getReference("user/" + username).child("ownedPots").push().setValue(helioID);

                    next();

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

    private void next(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(AddPlant.this);
//        builder.setCancelable(true);
//        builder.setTitle("Wifi connection");
//        builder.setMessage("Can HeiobsloPot connect to your home Wifi?");
//
//        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                onBackPressed();
//            }
//        });
//
//        builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                goToWifiSetting();
//            }
//        });
//        builder.show();
        if (setupType.equals("MINOR")){
            onBackPressed();
        }else{
            goToWifiSetting();
        }

    }

    private void goToWifiSetting(){
        Intent intent = new Intent(this, WifiSetup.class);
        startActivity(intent);
    }

    public void goToManualControl(View view) {
        Intent intent = new Intent(this, ManualControlFullSetup.class);
        startActivity(intent);
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

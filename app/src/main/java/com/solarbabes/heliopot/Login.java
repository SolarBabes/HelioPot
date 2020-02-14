package com.solarbabes.heliopot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;


public class Login extends AppCompatActivity {
//    private static final String FILE_NAME = "example.txt";

    EditText mEditText;
    private EditText Name;
    private EditText Password;
    private Button Login;
    private static final String TAG = "login";
    public static final String EXTRA_MESSAGE = "com.solarbabes.heliopot.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("solarbabesdb");
//        myRef.setValue("Hello, World!");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("bot/plant1/realtime");
//        mDatabase.child("000").child("999").setValue("11").addOnSuccessListener(new OnSuccessListener<Void>(){
//            @Override
//            public void onSuccess(Void aVoid) {
//                Log.d("111","23");
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d("666",e.toString());
//            }
//        });
//        mDatabase.child("bot").child("1").child("light").child("20202051915").setValue("14");
//        Random rand = new Random();
//        long a = System.currentTimeMillis();
//        for (int i = 0; i < 50; i++) {
//            a=a-500000;
//            mDatabase.child("bot").child("1").child("wateringtime").child(Long.toString(a)).setValue(Integer.toString(rand.nextInt(20)));
//            mDatabase.child("bot").child("1").child("temperature").child(Long.toString(a)).setValue(Integer.toString(rand.nextInt(20)));
//        }
        Log.d("123",mDatabase.child("pots").child("666").getKey());



        ValueEventListener Listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                Log.d("123"," "+dataSnapshot.getKey());
//                Log.d("123",dataSnapshot.getRef().toString());
                Log.d("123",dataSnapshot.getValue().toString());
//                Map<String, Integer> map = (Map<String, Integer>) dataSnapshot.getValue();
//                Log.d("map",map.toString());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("123", "loadPost:onCancelled", databaseError.toException());
            }
        };
//        mDatabase.addValueEventListener(Listener);












        Name = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.login);

        String username = load("username.txt");
        username=username.replaceAll("[^a-zA-Z0-9]","");


        if (username!=null && !username.equals("")){
//            Log.v(TAG, username);
            Name.setText(username);
            Toast.makeText(getApplicationContext(),"Welcome Back!"+username, Toast.LENGTH_LONG).show();
            gotoList(username);
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void gotoList(String username){
        Intent intent = new Intent(this, PlantList.class);
        String message = username;
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
        finish();
    }


    private void validate(String username, String userPassword){
        username=username.replaceAll("[^a-zA-Z0-9]","");
        if((username.equals("Admin")) && (userPassword.equals("1234"))||((!username.equals(""))&&username.equals(userPassword))){

            save("username.txt", username);
            gotoList(username);


        }else{
            Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
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

    public String load(String name) {
        FileInputStream fis = null;
        String text = "";

        try {
            fis = openFileInput(name);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();


            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }
            text = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    return text;
                }
            }
            return text;
        }

    }
}

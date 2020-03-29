package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.util.Log;

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
import java.security.DigestException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

import java.security.MessageDigest;


public class Login extends AppCompatActivity {
//    private static final String FILE_NAME = "example.txt";

    EditText mEditText;
    private EditText Name;
    private EditText Password;
    private Button Login;
    private Button Register;
    private String username="";
    private String password="";
    private static final String TAG = "login";
    public static final String USERNAME = "com.solarbabes.heliopot.MESSAGE";
    private int flag = 0;
    private HashMap<String, String> userinfo = new HashMap<>();
    private ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                if (!userinfo.containsKey(postSnapshot.getKey())){
                    try{
                        userinfo.put(postSnapshot.getKey(),postSnapshot.child("password").getValue().toString());
                    }catch (Exception e){
                        Log.d("login","login");
                    }

                }
            }
        }
        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.w("123", "loadPost:onCancelled", databaseError.toException());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbarlogin);
        toolbar.setTitle("HelioPot");
        setSupportActionBar(toolbar);

        Name = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.login);
        Register = (Button) findViewById(R.id.register);
        username = load("username.txt");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user");

        mDatabase.addValueEventListener(Listener);

//        Toast.makeText(getApplicationContext(), "Not more than 99", Toast.LENGTH_SHORT).show();

//        username=username.replaceAll("[^a-zA-Z0-9]","");


        if (username!=null && !username.equals("")){
            Name.setText(username);
            Toast.makeText(getApplicationContext(),"Welcome back, "+username+"!", Toast.LENGTH_LONG).show();
            gotoList(username);
        }


//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("solarbabesdb");
//        myRef.setValue("Hello, World!");
//        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("bot/plant1/realtime");
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
//        Log.d("123",mDatabase.child("pots").child("666").getKey());
//        mDatabase.addValueEventListener(Listener);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Name.setText(Name.getText().toString().replace(System.lineSeparator(),""));
                Password.setText(Password.getText().toString().replace(System.lineSeparator(),""));
                username = Name.getText().toString();
                password = Password.getText().toString();
                password = hash(password);
                Log.d("1",hash("billy"));
                Log.d("2",hash("billy"));

                if (username.matches(".*[^0-9a-zA-Z].*")){
                    Toast.makeText(getApplicationContext(),"Sorry, username can only contains 0-9a-zA-Z", Toast.LENGTH_LONG).show();
                    Name.setText("");
                }else if(password.matches(".*[^0-9a-zA-Z].*")){
                    Toast.makeText(getApplicationContext(),"Sorry, password can only contains 0-9a-zA-Z", Toast.LENGTH_LONG).show();
                    Password.setText("");
                }else{
                    validate(username,password);
                }
            }
        });

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name.setText(Name.getText().toString().replace(System.lineSeparator(),""));
                Password.setText(Password.getText().toString().replace(System.lineSeparator(),""));
                username = Name.getText().toString();
                password = Password.getText().toString();
                password = hash(password);
                if (username.length()== 0 ){
                    Toast.makeText(getApplicationContext(), "Username cannot be NULL.", Toast.LENGTH_SHORT).show();
                }else if (userinfo.containsKey(username)){
                    Toast.makeText(getApplicationContext(), Name.getText().toString()+" has been registered, pleanse sign in", Toast.LENGTH_SHORT).show();
                }else{
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user");
                    mDatabase.child(username).child("password").setValue(password);
                    Toast.makeText(getApplicationContext(), "Register succeed", Toast.LENGTH_SHORT).show();
                    save("username.txt", username);
                    gotoList(username);
                }
            }
        });
    }

    public String hash(String s){
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(s.getBytes());
            byte messageDigest[]= md.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i=0;i<messageDigest.length;i++){
                hexString.append(Integer.toHexString(0xFF&messageDigest[i]));
            }
            return hexString.toString();
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return "";
    }

    private void gotoList(String username){
        Intent intent = new Intent(this, PlantList.class);
        String message = username;
        intent.putExtra(USERNAME, message);
        startActivity(intent);
        finish();
    }



    private boolean checkFirebase(String username, String password){
        while (userinfo.size()==0){
            try {
                Thread.sleep(1000);
                Toast.makeText(getApplicationContext(), "Bad internet", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.d("info",Boolean.toString(userinfo.containsKey(username)));//TODO if new user click sign in
        if (userinfo.containsKey(username)){
            Log.d("p1",password);
            Log.d("p2",userinfo.get(username));

            if (password.equals(userinfo.get(username))){
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(getApplicationContext(), "User does not exist, please register first.", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private void validate(String username, String userPassword){
        username=username.replaceAll("[^a-zA-Z0-9]","");
        if(checkFirebase(username, userPassword)){
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

            text = br.readLine();

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

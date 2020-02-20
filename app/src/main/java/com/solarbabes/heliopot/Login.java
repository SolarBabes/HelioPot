package com.solarbabes.heliopot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.text.InputFilter;
import android.text.Spanned;
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
    private int flag = 0;
    private HashMap<String, String> userinfo = new HashMap<>();
    private ValueEventListener Listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                if (!userinfo.containsKey(postSnapshot.getKey())){
                    userinfo.put(postSnapshot.getKey(),postSnapshot.child("password").getValue().toString());
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

        Name = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.login);

        final String username = load("username.txt");

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user");

        mDatabase.addValueEventListener(Listener);

//        Toast.makeText(getApplicationContext(), "Not more than 99", Toast.LENGTH_SHORT).show();

//        username=username.replaceAll("[^a-zA-Z0-9]","");


        if (username!=null && !username.equals("")){
            Name.setText(username);
            Toast.makeText(getApplicationContext(),"Welcome Back!"+username, Toast.LENGTH_LONG).show();
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
                if (Name.getText().toString().matches(".*[^0-9a-zA-Z].*")){
                    Toast.makeText(getApplicationContext(),"Sorry, username can only contains 0-9a-zA-Z", Toast.LENGTH_LONG).show();
                    Name.setText("");
                }else if(Password.getText().toString().matches(".*[^0-9a-zA-Z].*")){
                    Toast.makeText(getApplicationContext(),"Sorry, password can only contains 0-9a-zA-Z", Toast.LENGTH_LONG).show();
                    Password.setText("");
                }else{
                    validate(Name.getText().toString(), Password.getText().toString());
                }
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



    private boolean checkFirebase(String username, String password){
        while (username.length()==0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (userinfo.containsKey(username)){
            if (password.equals(userinfo.get(username))){
                return true;
            }else{
                Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("user");
            mDatabase.child(username).child("password").setValue(password);
            Toast.makeText(getApplicationContext(), "Register succeed", Toast.LENGTH_SHORT).show();
            return true;
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

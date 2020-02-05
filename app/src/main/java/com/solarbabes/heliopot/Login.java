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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;


public class Login extends AppCompatActivity {
//    private static final String FILE_NAME = "example.txt";

    EditText mEditText;
    private EditText Name;
    private EditText Password;
    private Button Login;
    private static final String TAG = "login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Write a message to the database
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("solarbabesdb");
//        myRef.setValue("Hello, World!");
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
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
        










        Name = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.login);

        String username = load("username.txt");


        if (1==2&&username!=null && !username.equals("")){
//            Log.v(TAG, username);
            Name.setText(username);
            Intent intent = new Intent(this, PlantList.class);
            Toast.makeText(getApplicationContext(),"Welcome Back!"+username, Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
        }

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(String userName, String userPassword){
        if((userName.equals("Admin")) && (userPassword.equals("1234"))||((!userName.equals(""))&&userName.equals(userPassword))){
            Intent intent = new Intent(this, PlantList.class);
            save("username.txt", userName);

//            Toast.makeText(getApplicationContext(), load("username.txt"), Toast.LENGTH_LONG).show();
            startActivity(intent);
            finish();
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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

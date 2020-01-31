package com.solarbabes.heliopot;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Login extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Name = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.login);


        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });
    }

    private void validate(String userName, String userPassword){
        if((userName.equals("Admin")) && (userPassword.equals("1234"))||(userName.equals(userPassword))){
            Intent intent = new Intent(this, PlantList.class);
            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        }else{
            Toast.makeText(getApplicationContext(), "Wrong username or password", Toast.LENGTH_SHORT).show();
        }
    }
}

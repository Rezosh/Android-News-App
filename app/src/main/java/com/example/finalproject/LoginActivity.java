package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    Button loginBtn;
    Button registerBtn;

//  TODO: Need to add progress bars
//  TODO: When login btn is pressed check if valid login and then start activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginBtn = findViewById(R.id.activityLogin_LoginBtn);
        registerBtn = findViewById(R.id.activityLogin_RegisterBtn);

        Intent registerActivity = new Intent(this, RegisterActivity.class);
        Intent mainActivity = new Intent(this, MainActivity.class);

        registerBtn.setOnClickListener( click -> startActivity(registerActivity));

        loginBtn.setOnClickListener(click -> {
            Toast.makeText(click.getContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
            startActivity(mainActivity);
        });

    }
}

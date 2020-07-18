package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    public static final String ACTIVITY_NAME = "LoginActivity";
    Button loginBtn;
    Button registerBtn;
    CheckBox rememberMe;
    EditText emailField;
    EditText passwordField;
    private SharedPreferences.Editor loginPrefsEditor;

//  TODO: Need to add progress bars
//  TODO: When login btn is pressed check if valid login and then start activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Find buttons in layout
        loginBtn = findViewById(R.id.activityLogin_LoginBtn);
        registerBtn = findViewById(R.id.activityLogin_RegisterBtn);
        emailField = findViewById(R.id.activityLogin_email);
        rememberMe = findViewById(R.id.activityLogin_rememberMeCheckbox);
        passwordField = findViewById(R.id.activityLogin_password);
        SharedPreferences loginPreferences = getSharedPreferences("loginPrefs", MODE_PRIVATE);
        loginPrefsEditor = loginPreferences.edit();

        boolean saveLogin = loginPreferences.getBoolean("saveLogin", false);

        if (saveLogin) {
            emailField.setText(loginPreferences.getString("email", ""));
            passwordField.setText(loginPreferences.getString("password", ""));
            rememberMe.setChecked(true);
        }


        // When button is clicked it starts registerActivity
        registerBtn.setOnClickListener( click -> {
            Intent registerActivity = new Intent(this, RegisterActivity.class);
            startActivity(registerActivity);
        });

        // When button is clicked it starts mainActivity
        loginBtn.setOnClickListener(click -> {
            Toast.makeText(click.getContext(), "Logged in successfully!", Toast.LENGTH_SHORT).show();
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        });



        rememberMe.setOnCheckedChangeListener( (rememberMe, click) -> {

            String email = emailField.getText().toString();
            String password = passwordField.getText().toString();

            if (rememberMe.isChecked()) {
                loginPrefsEditor.putBoolean("saveLogin", true);
                loginPrefsEditor.putString("email", email);
                loginPrefsEditor.putString("password", password);
                loginPrefsEditor.apply();
            } else {
                loginPrefsEditor.clear();
                loginPrefsEditor.apply();
            }

        });
        Log.i(ACTIVITY_NAME, "In function: " + "onCreate");
    }

}

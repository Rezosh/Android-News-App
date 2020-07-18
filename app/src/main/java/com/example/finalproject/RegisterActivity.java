package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private static final String ACTIVITY_NAME = "RegisterActivity";
    Button createAccount;
    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    EditText confirmPassword;

    //    TODO: Need to add progress bars

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        createAccount = findViewById(R.id.activityRegister_createAccountBtn);
        firstName = findViewById(R.id.activityRegister_firstName);
        lastName = findViewById(R.id.activityRegister_lastName);
        email = findViewById(R.id.activityRegister_email);
        password = findViewById(R.id.activityRegister_password);
        confirmPassword = findViewById(R.id.activityRegister_confirmPassword);

        // TODO: Save values from EditText views to DB
        createAccount.setOnClickListener( click -> {

            Toast.makeText(click.getContext(), R.string.Account_Created, Toast.LENGTH_SHORT).show();
            // Close activity once account is saved.
            finish();
        });

        Log.i(ACTIVITY_NAME, "In function: " + "onCreate");
    }
}

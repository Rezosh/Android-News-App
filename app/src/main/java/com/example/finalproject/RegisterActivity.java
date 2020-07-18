package com.example.finalproject;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
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
    SQLiteDatabase db;

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

        createAccount.setOnClickListener( click -> {

            DBConnection dbConnection = new DBConnection(this);
            db = dbConnection.getWritableDatabase();
            // Save text to string variables
            String user_fName = firstName.getText().toString();
            String user_lName = lastName.getText().toString();
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            String userConfirmPassword = confirmPassword.getText().toString();

            // Checks
            if (!userConfirmPassword.equals(userPassword)) {
                confirmPassword.setError("Passwords do not match");
                return;
            }
            if (user_fName.matches("")){
                firstName.setError("Field is required");
                return;
            }
            if (user_lName.matches("")){
                firstName.setError("Field is required");
                return;
            }
            if (userEmail.matches("")){
                firstName.setError("Field is required");
                return;
            }
            if (userPassword.matches("")){
                firstName.setError("Field is required");
                return;
            }
            if (userConfirmPassword.matches("")){
                firstName.setError("Field is required");
                return;
            }
            // Save credentials in db.
            ContentValues newRowValues = new ContentValues();
            newRowValues.put(DBConnection.COL_FIRST_NAME, user_fName);
            newRowValues.put(DBConnection.COL_LAST_NAME, user_lName);
            newRowValues.put(DBConnection.COL_EMAIL, userEmail);
            newRowValues.put(DBConnection.COL_PASS, userPassword);
            db.insert(DBConnection.USERS_TABLE_NAME, null, newRowValues);
            // Reset fields
            firstName.setText("");
            lastName.setText("");
            email.setText("");
            password.setText("");
            // Notify user the account was made and close the activity.
            Toast.makeText(click.getContext(), R.string.Account_Created, Toast.LENGTH_SHORT).show();
            finish();
        });

        Log.i(ACTIVITY_NAME, "In function: " + "onCreate");
    }
}

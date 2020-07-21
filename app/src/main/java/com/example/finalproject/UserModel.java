package com.example.finalproject;

/**
 * Model used to save Users
 */
public class UserModel {
    public long userId;
    public String fName;
    public String lName;
    public String email;
    public String password;

    public UserModel(long userId, String fName, String lName, String email, String password) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
    }
}

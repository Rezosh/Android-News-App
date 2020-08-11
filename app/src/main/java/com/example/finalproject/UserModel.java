package com.example.finalproject;

/**
 * Model used to save Users
 */
public class UserModel {
    private long userId;
    private String fName;
    private String lName;
    private String email;
    private String password;

    public UserModel(long userId, String fName, String lName, String email, String password) {
        this.userId = userId;
        this.fName = fName;
        this.lName = lName;
        this.email = email;
        this.password = password;
    }


    // setters for the components of User
    public void setUserId(long x){
        this.userId = x;
    }

    public void  setfName(String x){
        this.fName = x;
    }
    public void setlName(String x){
        this.lName = x;
    }

    public void setMail(String x){
        this.email=x;
    }
    public void setPass(String x){
        this.password =x;
    }




    // getters for components of users
    public long getUserId(){
        return this.userId;
    }

    public String getfName(){
        return this.fName;
    }
    public String getlName(){
        return this.lName;
    }
    public String getMail(){
        return this.email;
    }
    public String getPass(){
        return this.password;
    }
}

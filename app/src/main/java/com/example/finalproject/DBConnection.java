package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

//    TODO: Make db tables and queries.
    private final static String DATABASE_NAME = "GuardianDB";
    private final static int VERSION_NUM = 1;
    public final static String FAVORITE_TABLE = "Favorites";

    public final static String USERS_TABLE = "Users";
    public final static String COL_EMAIL = "Email";
    public final static String COL_PASS = "Password";

    DBConnection(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

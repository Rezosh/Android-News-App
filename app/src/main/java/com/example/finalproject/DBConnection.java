package com.example.finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper {

//    TODO: Make db tables and queries.
    private final static String DATABASE_NAME = "GuardianDB";
    private final static int VERSION_NUM = 1;
    public final static String FAVORITE_TABLE_NAME = "Favorites";
    public final static String COL_ARTICLE_ID = "Article_id";
    public final static String COL_ARTICLE_ENDPOINT_ID = "Article_Endpoint_ID";

    public final static String USERS_TABLE_NAME = "Users";
    public final static String COL_USER_ID = "User_id";
    public final static String COL_EMAIL = "Email";
    public final static String COL_PASS = "Password";
    public final static String COL_FIRST_NAME = "firstName";
    public final static String COL_LAST_NAME = "lastName";

    String createUserTable = "CREATE TABLE " + USERS_TABLE_NAME +
            " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_EMAIL + " TEXT, "
            + COL_PASS + " TEXT, "
            + COL_FIRST_NAME +" TEXT, "
            + COL_LAST_NAME + " TEXT"+");";

    String createFavoritesTable = "CREATE TABLE " +
            FAVORITE_TABLE_NAME + " (" +
            COL_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL_USER_ID + " INTEGER NOT NULL, " +
            COL_ARTICLE_ENDPOINT_ID + " TEXT, FOREIGN KEY ("+
            COL_USER_ID+") REFERENCES "+
            USERS_TABLE_NAME+"("+COL_USER_ID+"));";

    public DBConnection(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(createUserTable);
        db.execSQL(createFavoritesTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + USERS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_TABLE_NAME);
        onCreate(db);
    }

}

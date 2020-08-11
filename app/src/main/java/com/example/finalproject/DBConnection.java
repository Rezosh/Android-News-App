package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBConnection extends SQLiteOpenHelper {

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
    private static final String COL_TITLE = "article_Title";
    private static final String COL_THUMBNAIL = "article_Thumbnail";
    private static final String COL_SECTION = "article_Section";
    private static final String COL_URL = "article_URL";

    String createUserTable = "CREATE TABLE " + USERS_TABLE_NAME +
            " (" + COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_EMAIL + " TEXT, "
            + COL_PASS + " TEXT, "
            + COL_FIRST_NAME +" TEXT, "
            + COL_LAST_NAME + " TEXT"+");";

    String createFavoritesTable = "CREATE TABLE " +
            FAVORITE_TABLE_NAME + " (" +
            COL_ARTICLE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COL_ARTICLE_ENDPOINT_ID + " TEXT,"+ COL_TITLE+" TEXT,"
            + COL_SECTION + " TEXT," + COL_THUMBNAIL + " TEXT," + COL_URL + " TEXT);";

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

    /**
     * Insert a user into the database
     * @param queryValues User object
     */
    public void insertUser (UserModel queryValues) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newRowValues = new ContentValues();

        newRowValues.put(COL_FIRST_NAME, queryValues.getfName());
        newRowValues.put(COL_LAST_NAME, queryValues.getlName());
        newRowValues.put(COL_EMAIL, queryValues.getMail());
        newRowValues.put(COL_PASS, queryValues.getPass());

        queryValues.setUserId(db.insert(USERS_TABLE_NAME, null, newRowValues)) ;
        db.close();
    }

    /**
     *  Inserts a users favorite articles into the database
     * @param queryValues Values to insert into the database
     */
    public void insertArticle (ArticleModel queryValues) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues newRowValues = new ContentValues();

        newRowValues.put(COL_ARTICLE_ENDPOINT_ID, queryValues.getEndpoint());
        newRowValues.put(COL_TITLE, queryValues.getTitle());
        newRowValues.put(COL_THUMBNAIL, queryValues.getThumbnail());
        newRowValues.put(COL_SECTION, queryValues.getSection());
        newRowValues.put(COL_URL, queryValues.getUrl());

        long newId = db.insert(FAVORITE_TABLE_NAME, null, newRowValues);
        System.out.println(newId);
        db.close();
    }

    public void deleteArticle (String endpointId) {

        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(FAVORITE_TABLE_NAME, COL_ARTICLE_ENDPOINT_ID + "=?", new String[] {endpointId});
        db.close();
    }


    public List<ArticleModel> getArticle () {
        SQLiteDatabase db = this.getReadableDatabase();

        List<ArticleModel> articleList = new ArrayList<>();
        String query = "Select * from "+FAVORITE_TABLE_NAME+"";
        Cursor results = db.rawQuery(query, null);
        int endpointColumnIndex = results.getColumnIndex(COL_ARTICLE_ENDPOINT_ID);
        int titleColumnIndex = results.getColumnIndex(COL_TITLE);
        int thumbnailColumnIndex = results.getColumnIndex(COL_THUMBNAIL);
        int sectionColumnIndex = results.getColumnIndex(COL_SECTION);
        int urlColumnIndex = results.getColumnIndex(COL_URL);

        while(results.moveToNext()) {

            String endpoint = results.getString(endpointColumnIndex);
            String title = results.getString(titleColumnIndex);
            String thumbnail = results.getString(thumbnailColumnIndex);
            String section = results.getString(sectionColumnIndex);
            String url = results.getString(urlColumnIndex);
            ArticleModel articleModel = new ArticleModel(endpoint, title, url, thumbnail, section);
            articleList.add(articleModel);
        }
        results.close();
        return articleList;
    }



    /**
     * Used to get a user from the database
     * @param email User email address
     * @return UserModel with results from the database
     */
    public UserModel getUser (String email){
        SQLiteDatabase db = this.getReadableDatabase();
        long userId = 0;
        String fName = "";
        String lName = "";
        String password = "";


        String query = "Select * from Users where Email ='"+email+"'";
        Cursor results = db.rawQuery(query, null);

        int userIdColumnIndex = results.getColumnIndex(COL_USER_ID);
        int fNameColumnIndex = results.getColumnIndex(COL_FIRST_NAME);
        int lNameColumnIndex = results.getColumnIndex(COL_LAST_NAME);
        int passwordColumnIndex = results.getColumnIndex(COL_PASS);

        while(results.moveToNext()) {

            //
           userId = results.getLong(userIdColumnIndex);
           fName = results.getString(fNameColumnIndex);
           lName = results.getString(lNameColumnIndex);
           password = results.getString(passwordColumnIndex);




        }
        UserModel myUserModel = new UserModel(userId, fName, lName, email, password);
        System.out.println("USER: " + password);


        results.close();
        return myUserModel;
    }

}

package com.msd.finalproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import androidx.annotation.Nullable;

import com.msd.finalproject.model.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Creating Employee Tracking Database
    public static final String DB_NAME = "EmpTracking.db";

    // Database version
    public static final int VERSION = 1;

    // User Table and it's columns
    public static final String USER_TABLE_NAME = "Users";

    public static final String USER_COL1 = "id";
    public static final String USER_COL2 = "username";
    public static final String USER_COL3 = "password";

    // Activity Table and it's columns
    public static final String ACTIVITY_TABLE_NAME = "Activity";
    public static final String ACTIVITY_COL1 = "id";
    public static final String ACTIVITY_COL2 = "userId";
    public static final String ACTIVITY_COL3 = "imagePath";
    public static final String ACTIVITY_COL4 = "createdAt";

    // Coordinates Table and it's columns
    public static final String COORDIATES_TABLE_NAME = "Coordinates";
    public static final String COORDIATES_COL1 = "id";
    public static final String COORDIATES_COL2 = "activityId";
    public static final String COORDIATES_COL3 = "longitude";
    public static final String COORDIATES_COL4 = "latitude";

    // Initialising create user table query
    public static final String CREATE_USER_TABLE = "create table " + USER_TABLE_NAME + "(" + USER_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_COL2 + " TEXT NOT NULL, " + USER_COL3 + " TEXT NOT NULL);";

    // Initialising create activity table query
    public static final String CREATE_ACTIVITY_TABLE = "create table " + ACTIVITY_TABLE_NAME + " (" + ACTIVITY_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            ACTIVITY_COL3 + " TEXT NOT NULL, " + ACTIVITY_COL4 + " TEXT NOT NULL," + ACTIVITY_COL2 + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + ACTIVITY_COL2 + ") REFERENCES " + USER_TABLE_NAME + "(" + USER_COL1 + "));";

    // Initialising create activity table query
    public static final String CREATE_COORDINATES_TABLE = "create table " + COORDIATES_TABLE_NAME + " (" + COORDIATES_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COORDIATES_COL3 + " REAL NOT NULL, " + COORDIATES_COL4 + " REAL NOT NULL," + COORDIATES_COL2 + " INTEGER NOT NULL, " +
            "FOREIGN KEY (" + COORDIATES_COL2 + ") REFERENCES " + ACTIVITY_TABLE_NAME + "(" + ACTIVITY_COL1 + "));";

    // Initialising drop user table query
    public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + ACTIVITY_TABLE_NAME;

    // Initialising drop user table query
    public static final String DROP_ACTIVITY_TABLE = "DROP TABLE IF EXISTS " + ACTIVITY_TABLE_NAME;

    // Initialising drop coordinates table query
    public static final String DROP_COORDINATES_TABLE = "DROP TABLE IF EXISTS " + ACTIVITY_TABLE_NAME;

    public DataBaseHelper(@Nullable Context context) {
        // Initialising database
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating table definition
        db.execSQL(CREATE_USER_TABLE);
        db.execSQL(CREATE_ACTIVITY_TABLE);
        db.execSQL(CREATE_COORDINATES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // updating table definition
        db.execSQL(DROP_USER_TABLE);
        db.execSQL(DROP_ACTIVITY_TABLE);
        db.execSQL(DROP_COORDINATES_TABLE);
        onCreate(db);
    }

    /**
     * Method to populate user data in user table
     * only if data doesn't exists in table already
     */
    public void populateUserData() {
        // statically created user data
        List<User> users = new ArrayList<User>();
        users.add(new User("ankitjshah99@gmail.com", "123456"));
        users.add(new User("rajat@gmail.com", "admin"));
        users.add(new User("kaushal@gmail.com", "12345"));
        users.add(new User("unnati@gmail.com", "12345"));

        if (!checkIfUserDataExists()) {
            insertUserRecords(users);
        }
    }

    /*
    Code to insert user data into database
    values are assigned to fields in key - value pair with the help of content values
    we will populate user data for only once, that too, statically
     */
    private boolean insertUserRecords(List<User> users) {
        long result = -1;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        for (User user : users) {
            contentValues.put(USER_COL2, user.getUserName());
            contentValues.put(USER_COL3, user.getPassword());
            result = db.insert(USER_TABLE_NAME, null, contentValues);
        }

        return result != -1;
    }

    /*
    Method to check whether user data exists or not
     */
    private boolean checkIfUserDataExists() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME, null);

        return cursor.getCount() > 0;
    }

    /*
    Validating user credentials to authorise the login process
    */
    public User validateUserCredentials(String userName, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " where userName = " + "'" + userName + "'" + "AND password = " + "'" + password + "'", null);

        User user = new User();
        if (cursor.moveToFirst()) {
            do {
                // adding authenticated user's data to object
                user.setId(cursor.getInt(cursor.getColumnIndex("id")));
                user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
                user.setUserName(cursor.getString(cursor.getColumnIndex("username")));
            } while (cursor.moveToNext());
        }
        return user;
    }

    public Long storeActivityDetails(String loggedInUser, String encodedImage) {

        SQLiteDatabase db = this.getWritableDatabase();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String date = sdf.format(new Date());
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.ACTIVITY_COL3, encodedImage);
        cv.put(DataBaseHelper.ACTIVITY_COL2, loggedInUser);
        cv.put(DataBaseHelper.ACTIVITY_COL4, date);

        return db.insert(DataBaseHelper.ACTIVITY_TABLE_NAME, null, cv);

    }

    public Long storeActivityCoordinates(Location location, Integer activityId) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.COORDIATES_COL3, location.getLongitude());
        cv.put(DataBaseHelper.COORDIATES_COL2, activityId);
        cv.put(DataBaseHelper.COORDIATES_COL4, location.getLatitude());

        return db.insert(DataBaseHelper.COORDIATES_TABLE_NAME, null, cv);

    }

    /**
     * Code to delete captured image from the database
     *
     * @param row reflects uid of image stored
     */
    public void deleteEntry(long row) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(ACTIVITY_TABLE_NAME, ACTIVITY_COL1 + "=" + row, null);
    }
}

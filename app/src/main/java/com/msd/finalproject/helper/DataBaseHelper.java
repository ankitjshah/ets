package com.msd.finalproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.msd.finalproject.model.User;

import java.util.ArrayList;
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

    // Initialising create user table query
    public static final String CREATE_USER_TABLE = "create table " + USER_TABLE_NAME + "(" + USER_COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            USER_COL2 + " TEXT NOT NULL, " + USER_COL3 + " TEXT NOT NULL);";


    // Initialising drop user table query
    public static final String DROP_USER_TABLE = "DROP TABLE IF EXISTS " + USER_TABLE_NAME;

    public DataBaseHelper(@Nullable Context context) {
        // Initialising database
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating table definition
        db.execSQL(CREATE_USER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // updating table definition
        db.execSQL(DROP_USER_TABLE);
        onCreate(db);
    }

    public void populateUserData() {
        List<User> users = new ArrayList<User>();
        users.add(new User("ankit", "123456"));
        users.add(new User("karishma", "admin"));
        users.add(new User("kaushal", "12345"));

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
    public Boolean validateUserCredentials(String userName, String password) {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + USER_TABLE_NAME + " where userName = " + "'" + userName + "'" + "AND password = " + "'" + password + "'", null);

        return cursor.getCount() > 0;
    }
    /*public boolean insertGrade(User grade) {
     *//*
        Code to insert data into database
        values are assigned to fields in key - value pair with the help of content values
         *//*
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, grade.getFirstName());
        contentValues.put(COL3, grade.getLastName());
        contentValues.put(COL4, grade.getCourseName());
        contentValues.put(COL5, grade.getCredit());
        contentValues.put(COL6, grade.getMarks());

        long result = db.insert(CREATE_USER_TABLE, null, contentValues);

        return result != -1;
    }

    public Cursor viewGrades() {
        *//*
        Code to select all data from the database
         *//*
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Integer deleteGrade(int id) {
    *//*
        COde to delete records based on the id from the database
     *//*
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, " id = " + id, null);
    }

    public Cursor searchGrade(String value, String filter) {
        *//*
        Code to search database records based on filters selected by users
        for example if filter is id then numeric id value will be provided
                    if course is selected then course code will be provided
                        by user from spinner on the screen
         *//*
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        if ("id".equalsIgnoreCase(filter)) {

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where id = " + Integer.parseInt(value), null);
        } else {

            cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " where course = " + "'" + value + "'", null);

        }

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public int updateGrade(Grades grade) {
        *//*
        Code to update data into database
        values are assigned to fields in key - value pair with the help of content values
         *//*
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, grade.getFirstName());
        contentValues.put(COL3, grade.getLastName());
        contentValues.put(COL4, grade.getCourseName());
        contentValues.put(COL5, grade.getCredit());
        contentValues.put(COL6, grade.getMarks());

        int numRows = db.update(TABLE_NAME, contentValues, " id = " + grade.getId(), null);
        return numRows;

    }*/
}

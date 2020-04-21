package com.msd.finalproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.msd.assignment2.model.Grades;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Declaration and Initialisation of variables
    public static final String DB_NAME = "EmpTracking.db";
    public static final int VERSION = 1;
    public static final String TABLE_NAME = "Grades";
    public static final String COL1 = "id";
    public static final String COL2 = "firstName";
    public static final String COL3 = "lastName";
    public static final String COL4 = "course";
    public static final String COL5 = "credit";
    public static final String COL6 = "marks";

    // Initialising create table query
    public static final String CREATE_TABLE = "create table " + TABLE_NAME + "(" + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COL2 + " TEXT NOT NULL, " + COL3 + " TEXT NOT NULL," + COL4 + " TEXT NOT NULL, " +
            COL5 + " INTEGER NOT NULL, " + COL6 + " INTEGER NOT NULL);";


    // Initialising drop table query
    public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public DataBaseHelper(@Nullable Context context) {
        // Initialising database
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Creating table definition
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // updating table definition
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public boolean insertGrade(Grades grade) {
        /*
        Code to insert data into database
        values are assigned to fields in key - value pair with the help of content values
         */
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, grade.getFirstName());
        contentValues.put(COL3, grade.getLastName());
        contentValues.put(COL4, grade.getCourseName());
        contentValues.put(COL5, grade.getCredit());
        contentValues.put(COL6, grade.getMarks());

        long result = db.insert(TABLE_NAME, null, contentValues);

        return result != -1;
    }

    public Cursor viewGrades() {
        /*
        Code to select all data from the database
         */
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor;
        cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }

    public Integer deleteGrade(int id) {
    /*
        COde to delete records based on the id from the database
     */
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, " id = " + id, null);
    }

    public Cursor searchGrade(String value, String filter) {
        /*
        Code to search database records based on filters selected by users
        for example if filter is id then numeric id value will be provided
                    if course is selected then course code will be provided
                        by user from spinner on the screen
         */
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
        /*
        Code to update data into database
        values are assigned to fields in key - value pair with the help of content values
         */
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, grade.getFirstName());
        contentValues.put(COL3, grade.getLastName());
        contentValues.put(COL4, grade.getCourseName());
        contentValues.put(COL5, grade.getCredit());
        contentValues.put(COL6, grade.getMarks());

        int numRows = db.update(TABLE_NAME, contentValues, " id = " + grade.getId(), null);
        return numRows;

    }
}
